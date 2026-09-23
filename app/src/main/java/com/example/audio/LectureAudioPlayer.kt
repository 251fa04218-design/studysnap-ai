package com.example.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
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
 * On-device audio player for reviewing recorded lecture audio with seek,
 * variable speed playback (0.75x - 2.0x), and real-time playback position tracking.
 */
class LectureAudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0)
    val currentPositionMs: StateFlow<Int> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0)
    val durationMs: StateFlow<Int> = _durationMs.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _activeFilePath = MutableStateFlow<String?>(null)
    val activeFilePath: StateFlow<String?> = _activeFilePath.asStateFlow()

    fun loadAndPlay(filePath: String, startOffsetMs: Int = 0) {
        val file = File(filePath)
        if (!file.exists() && !filePath.startsWith("android.resource")) {
            Log.w("LectureAudioPlayer", "Audio file not found at: $filePath")
            return
        }

        try {
            release()

            val player = MediaPlayer().apply {
                if (filePath.startsWith("android.resource")) {
                    setDataSource(context, Uri.parse(filePath))
                } else {
                    setDataSource(filePath)
                }
                prepare()
                if (startOffsetMs > 0 && startOffsetMs < duration) {
                    seekTo(startOffsetMs)
                }
                applySpeed(this, _playbackSpeed.value)
                start()
            }

            mediaPlayer = player
            _activeFilePath.value = filePath
            _durationMs.value = player.duration
            _currentPositionMs.value = player.currentPosition
            _isPlaying.value = true

            player.setOnCompletionListener {
                _isPlaying.value = false
                _currentPositionMs.value = _durationMs.value
                progressJob?.cancel()
            }

            player.setOnErrorListener { _, what, extra ->
                Log.e("LectureAudioPlayer", "MediaPlayer error: what=$what, extra=$extra")
                release()
                true
            }

            startProgressTracking()
        } catch (e: Exception) {
            Log.e("LectureAudioPlayer", "Failed to load/play audio: $filePath", e)
            release()
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                _isPlaying.value = false
            }
        }
    }

    fun resume() {
        mediaPlayer?.let { player ->
            if (!player.isPlaying) {
                applySpeed(player, _playbackSpeed.value)
                player.start()
                _isPlaying.value = true
                startProgressTracking()
            }
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.let { player ->
            val safePos = positionMs.coerceIn(0, _durationMs.value)
            player.seekTo(safePos)
            _currentPositionMs.value = safePos
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        val clamped = speed.coerceIn(0.5f, 2.5f)
        _playbackSpeed.value = clamped
        mediaPlayer?.let { player ->
            applySpeed(player, clamped)
        }
    }

    private fun applySpeed(player: MediaPlayer, speed: Float) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val params = player.playbackParams ?: PlaybackParams()
                params.speed = speed
                player.playbackParams = params
            }
        } catch (e: Exception) {
            Log.w("LectureAudioPlayer", "Could not apply playback params", e)
        }
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive && _isPlaying.value) {
                mediaPlayer?.let { player ->
                    try {
                        if (player.isPlaying) {
                            _currentPositionMs.value = player.currentPosition
                        }
                    } catch (e: Exception) {
                        // ignore state errors during teardown
                    }
                }
                delay(200)
            }
        }
    }

    fun release() {
        progressJob?.cancel()
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
        } catch (e: Exception) {
            // Ignore
        } finally {
            mediaPlayer = null
            _isPlaying.value = false
            _activeFilePath.value = null
            _currentPositionMs.value = 0
            _durationMs.value = 0
        }
    }
}
