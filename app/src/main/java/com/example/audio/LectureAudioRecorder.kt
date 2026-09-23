package com.example.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

/**
 * Audio quality presets tailored for classroom lectures and speech analysis.
 */
enum class AudioQualityPreset(
    val title: String,
    val bitrate: Int,
    val sampleRate: Int,
    val channels: Int,
    val subtitle: String
) {
    HD_LECTURE(
        title = "HD Lecture",
        bitrate = 192_000,
        sampleRate = 48_000,
        channels = 2,
        subtitle = "192 kbps • 48 kHz AAC Stereo (Recommended)"
    ),
    STUDIO_CLARITY(
        title = "Studio Clarity",
        bitrate = 256_000,
        sampleRate = 48_000,
        channels = 2,
        subtitle = "256 kbps • 48 kHz AAC High-Fidelity"
    ),
    VOICE_SAVER(
        title = "Voice Saver",
        bitrate = 128_000,
        sampleRate = 44_100,
        channels = 1,
        subtitle = "128 kbps • 44.1 kHz AAC Mono (Low storage)"
    )
}

/**
 * Robust wrapper around Android's MediaRecorder API configured for high-fidelity
 * lecture capture, acoustic metering, and pause/resume lifecycle management.
 */
class LectureAudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var meteringJob: Job? = null

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _recordingDurationSeconds = MutableStateFlow(0)
    val recordingDurationSeconds: StateFlow<Int> = _recordingDurationSeconds.asStateFlow()

    private val _amplitude = MutableStateFlow(0f)
    val amplitude: StateFlow<Float> = _amplitude.asStateFlow()

    private val _amplitudeHistory = MutableStateFlow<List<Float>>(List(24) { 0.08f })
    val amplitudeHistory: StateFlow<List<Float>> = _amplitudeHistory.asStateFlow()

    private val _selectedPreset = MutableStateFlow(AudioQualityPreset.HD_LECTURE)
    val selectedPreset: StateFlow<AudioQualityPreset> = _selectedPreset.asStateFlow()

    private val _estimatedSizeBytes = MutableStateFlow(0L)
    val estimatedSizeBytes: StateFlow<Long> = _estimatedSizeBytes.asStateFlow()

    fun setQualityPreset(preset: AudioQualityPreset) {
        if (!_isRecording.value) {
            _selectedPreset.value = preset
        }
    }

    /**
     * Initializes and starts high-quality audio recording with the selected preset.
     */
    fun startRecording(preset: AudioQualityPreset = _selectedPreset.value): Boolean {
        if (_isRecording.value) return false

        _selectedPreset.value = preset

        return try {
            val lectureDir = File(context.filesDir, "lectures")
            if (!lectureDir.exists()) {
                lectureDir.mkdirs()
            }

            val audioFile = File(lectureDir, "lecture_${System.currentTimeMillis()}.m4a")
            outputFile = audioFile

            val newRecorder = createConfiguredRecorder(audioFile, preset)
            newRecorder.start()

            recorder = newRecorder
            _isRecording.value = true
            _isPaused.value = false
            _recordingDurationSeconds.value = 0
            _estimatedSizeBytes.value = 0L

            startMeteringJob(preset)
            true
        } catch (e: Exception) {
            Log.e("LectureAudioRecorder", "Failed to start MediaRecorder", e)
            cleanupRecorderInternal()
            false
        }
    }

    private fun createConfiguredRecorder(outputFile: File, preset: AudioQualityPreset): MediaRecorder {
        val mr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        // Prefer VOICE_RECOGNITION source for acoustic beamforming & noise reduction; fallback to MIC
        try {
            mr.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
        } catch (e: Exception) {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
        }

        mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mr.setAudioEncodingBitRate(preset.bitrate)
        mr.setAudioSamplingRate(preset.sampleRate)
        mr.setAudioChannels(preset.channels)
        mr.setOutputFile(outputFile.absolutePath)

        mr.setOnErrorListener { _, what, extra ->
            Log.e("LectureAudioRecorder", "MediaRecorder error: what=$what, extra=$extra")
            cleanupRecorderInternal()
        }

        mr.setOnInfoListener { _, what, extra ->
            Log.d("LectureAudioRecorder", "MediaRecorder info: what=$what, extra=$extra")
        }

        mr.prepare()
        return mr
    }

    private fun startMeteringJob(preset: AudioQualityPreset) {
        meteringJob?.cancel()
        meteringJob = CoroutineScope(Dispatchers.IO).launch {
            var elapsedMs = 0L
            val history = MutableList(24) { 0.08f }

            while (isActive && _isRecording.value) {
                delay(100)
                if (!_isPaused.value) {
                    elapsedMs += 100

                    val rawAmp = try {
                        recorder?.maxAmplitude ?: 0
                    } catch (e: Exception) {
                        0
                    }

                    val normalizedAmp = if (rawAmp > 0) {
                        (rawAmp / 32767f).coerceIn(0.05f, 1f)
                    } else {
                        // Keep a subtle natural fluctuation during silence
                        (kotlin.random.Random.nextFloat() * 0.15f) + 0.05f
                    }

                    _amplitude.value = normalizedAmp

                    // Rolling waveform update
                    history.removeAt(0)
                    history.add(normalizedAmp)
                    _amplitudeHistory.value = history.toList()

                    val seconds = (elapsedMs / 1000).toInt()
                    _recordingDurationSeconds.value = seconds

                    // Calculate estimated file size: (bitrate * seconds) / 8
                    val estimatedBytes = (preset.bitrate.toLong() * seconds) / 8
                    _estimatedSizeBytes.value = estimatedBytes
                }
            }
        }
    }

    /**
     * Pauses the active recording (API 24+).
     */
    fun pauseRecording(): Boolean {
        if (!_isRecording.value || _isPaused.value) return false
        return try {
            recorder?.pause()
            _isPaused.value = true
            true
        } catch (e: Exception) {
            Log.e("LectureAudioRecorder", "Failed to pause recorder", e)
            false
        }
    }

    /**
     * Resumes the paused recording (API 24+).
     */
    fun resumeRecording(): Boolean {
        if (!_isRecording.value || !_isPaused.value) return false
        return try {
            recorder?.resume()
            _isPaused.value = false
            true
        } catch (e: Exception) {
            Log.e("LectureAudioRecorder", "Failed to resume recorder", e)
            false
        }
    }

    /**
     * Stops the recording safely and returns the resulting audio file.
     */
    fun stopRecording(): File? {
        if (!_isRecording.value) return null
        meteringJob?.cancel()

        val captured = outputFile
        try {
            recorder?.apply {
                try {
                    stop()
                } catch (e: Exception) {
                    Log.e("LectureAudioRecorder", "Error during recorder.stop()", e)
                }
                release()
            }
        } catch (e: Exception) {
            Log.e("LectureAudioRecorder", "Failed to release recorder", e)
        } finally {
            recorder = null
            _isRecording.value = false
            _isPaused.value = false
        }

        return captured
    }

    /**
     * Cancels recording and deletes the temporary file.
     */
    fun cancelRecording() {
        meteringJob?.cancel()
        try {
            recorder?.apply {
                try { stop() } catch (e: Exception) {}
                release()
            }
        } catch (e: Exception) {
            Log.e("LectureAudioRecorder", "Cancel recorder error", e)
        } finally {
            recorder = null
            outputFile?.delete()
            outputFile = null
            _isRecording.value = false
            _isPaused.value = false
            _recordingDurationSeconds.value = 0
            _estimatedSizeBytes.value = 0L
        }
    }

    private fun cleanupRecorderInternal() {
        meteringJob?.cancel()
        try {
            recorder?.release()
        } catch (e: Exception) {
            // Ignore
        }
        recorder = null
        _isRecording.value = false
        _isPaused.value = false
    }
}
