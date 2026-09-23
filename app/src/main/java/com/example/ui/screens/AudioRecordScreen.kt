package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.AudioQualityPreset
import com.example.ui.theme.AmberGold
import com.example.ui.theme.HexagonCyan
import com.example.ui.theme.NpuGreen
import com.example.ui.theme.SnapdragonRed
import com.example.ui.theme.SnapdragonRedBright
import com.example.ui.theme.TechNavyBorder
import com.example.ui.theme.TechNavyCard
import com.example.ui.theme.TechNavyDark
import com.example.ui.theme.TechNavySurface
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun AudioRecordScreen(
    viewModel: StudyViewModel,
    onNavigateBack: () -> Unit,
    onProcessingFinished: () -> Unit
) {
    val context = LocalContext.current
    val recorder = viewModel.audioRecorder

    val isRecording by recorder.isRecording.collectAsStateWithLifecycle()
    val isPaused by recorder.isPaused.collectAsStateWithLifecycle()
    val durationSeconds by recorder.recordingDurationSeconds.collectAsStateWithLifecycle()
    val amplitudeHistory by recorder.amplitudeHistory.collectAsStateWithLifecycle()
    val selectedPreset by recorder.selectedPreset.collectAsStateWithLifecycle()
    val estimatedBytes by recorder.estimatedSizeBytes.collectAsStateWithLifecycle()
    val isAiProcessing by viewModel.isAiProcessing.collectAsStateWithLifecycle()

    var lectureTitle by remember { mutableStateOf("") }
    var lectureSubject by remember { mutableStateOf("") }
    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasMicPermission = granted
        if (granted) {
            recorder.startRecording()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = TechNavyDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isRecording) recorder.cancelRecording()
                        onNavigateBack()
                    },
                    modifier = Modifier.testTag("audio_record_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Lecture Audio Recorder",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "High-Fidelity MediaRecorder • Snapdragon Hexagon ASR",
                        style = MaterialTheme.typography.bodySmall,
                        color = HexagonCyan
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Zero-Cloud Privacy & Silicon Architecture Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = TechNavyCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NpuGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = NpuGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Zero Cloud Audio Transmission",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Acoustic audio is encoded via Android MediaRecorder and transcribed in Snapdragon NPU local memory.",
                            fontSize = 11.sp,
                            color = TextSecondaryDark,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Audio Quality Preset Selector (Disabled during active recording)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "RECORDING QUALITY PRESET",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = HexagonCyan,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AudioQualityPreset.values().forEach { preset ->
                        val isSelected = selectedPreset == preset
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) SnapdragonRedBright else TechNavyBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable(enabled = !isRecording) {
                                    recorder.setQualityPreset(preset)
                                }
                                .testTag("quality_preset_${preset.name.lowercase()}"),
                            color = if (isSelected) SnapdragonRed.copy(alpha = 0.2f) else TechNavyCard
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = preset.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextSecondaryDark
                                )
                                Text(
                                    text = "${preset.bitrate / 1000}k AAC",
                                    fontSize = 10.sp,
                                    color = if (isSelected) HexagonCyan else TextSecondaryDark
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Real-time Timer & Status Badge
            Text(
                text = String.format("%02d:%02d", durationSeconds / 60, durationSeconds % 60),
                fontSize = 54.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    isPaused -> AmberGold
                    isRecording -> SnapdragonRedBright
                    else -> Color.White
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                when {
                    isPaused -> {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(AmberGold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Recording Paused",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmberGold
                        )
                    }
                    isRecording -> {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(SnapdragonRedBright)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Live Classroom Recording (${selectedPreset.title})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SnapdragonRedBright
                        )
                    }
                    else -> {
                        Text(
                            text = "Ready to record with high-clarity MediaRecorder",
                            fontSize = 13.sp,
                            color = TextSecondaryDark
                        )
                    }
                }
            }

            // Real-time Size Estimation
            if (isRecording || durationSeconds > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                val sizeKb = estimatedBytes / 1024
                val sizeMb = sizeKb / 1024f
                val formattedSize = if (sizeMb >= 1.0f) String.format("%.2f MB", sizeMb) else "$sizeKb KB"
                Text(
                    text = "Local Storage: $formattedSize • ${selectedPreset.sampleRate / 1000} kHz ${if (selectedPreset.channels == 2) "Stereo" else "Mono"}",
                    fontSize = 11.sp,
                    color = HexagonCyan
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Real-time Rolling Waveform Visualizer
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                shape = RoundedCornerShape(14.dp),
                color = TechNavySurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    amplitudeHistory.forEach { amp ->
                        val targetHeight = if (isRecording && !isPaused) {
                            (12.dp + (68.dp * amp)).coerceIn(8.dp, 76.dp)
                        } else if (isPaused) {
                            14.dp
                        } else {
                            8.dp
                        }

                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(targetHeight)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isPaused) {
                                        Brush.verticalGradient(listOf(AmberGold, Color(0xFFB78103)))
                                    } else if (isRecording) {
                                        Brush.verticalGradient(listOf(SnapdragonRedBright, HexagonCyan))
                                    } else {
                                        Brush.verticalGradient(listOf(TechNavyBorder, TechNavyBorder))
                                    }
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Recording Controls: Pause/Resume + Main Record/Stop + Cancel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pause / Resume Button (Visible when recording)
                AnimatedVisibility(visible = isRecording) {
                    Row {
                        IconButton(
                            onClick = {
                                if (isPaused) {
                                    recorder.resumeRecording()
                                } else {
                                    recorder.pauseRecording()
                                }
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(TechNavySurface)
                                .border(1.dp, TechNavyBorder, CircleShape)
                                .testTag("pause_resume_recording_button")
                        ) {
                            Icon(
                                imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (isPaused) "Resume Recording" else "Pause Recording",
                                tint = if (isPaused) AmberGold else Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }

                // Main Action Button (Record / Stop & Synthesize)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            if (isRecording) {
                                Brush.linearGradient(listOf(SnapdragonRed, SnapdragonRedBright))
                            } else {
                                Brush.linearGradient(listOf(HexagonCyan, Color(0xFF007799)))
                            }
                        )
                        .border(3.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (!isRecording) {
                                if (!hasMicPermission) {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                } else {
                                    recorder.startRecording()
                                }
                            } else {
                                val capturedFile = recorder.stopRecording()
                                val title = lectureTitle.ifBlank { "Classroom Lecture ${System.currentTimeMillis() % 1000}" }
                                val subject = lectureSubject.ifBlank { "Recorded Lectures" }

                                viewModel.processRecordedLecture(
                                    title = title,
                                    subject = subject,
                                    durationSec = durationSeconds,
                                    audioFile = capturedFile,
                                    preset = selectedPreset
                                ) {
                                    onProcessingFinished()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("toggle_audio_record_button")
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = if (isRecording) "Stop Recording & Synthesize" else "Start Recording",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }

                // Discard Button (Visible when recording)
                AnimatedVisibility(visible = isRecording) {
                    Row {
                        Spacer(modifier = Modifier.width(20.dp))
                        IconButton(
                            onClick = {
                                recorder.cancelRecording()
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(TechNavySurface)
                                .border(1.dp, TechNavyBorder, CircleShape)
                                .testTag("discard_recording_button")
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF5350)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isRecording) "Tap Red Square to Finish & Synthesize" else "Tap Mic to Start High-Clarity Recording",
                fontSize = 12.sp,
                color = TextSecondaryDark
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Metadata inputs: Title & Subject
            OutlinedTextField(
                value = lectureTitle,
                onValueChange = { lectureTitle = it },
                label = { Text("Lecture Name / Topic") },
                placeholder = { Text("e.g. EE 330: Audio DSP & Acoustic Processing") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lecture_title_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SnapdragonRedBright,
                    unfocusedBorderColor = TechNavyBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lectureSubject,
                onValueChange = { lectureSubject = it },
                label = { Text("Course / Department") },
                placeholder = { Text("e.g. Electrical Engineering, Computer Science") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lecture_subject_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SnapdragonRedBright,
                    unfocusedBorderColor = TechNavyBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
