package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.StudyMaterial
import com.example.ui.screens.AudioRecordScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.MaterialDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TechNavyBorder
import com.example.ui.theme.TechNavyDark
import com.example.ui.viewmodel.StudyViewModel

sealed class AppScreen {
    object Dashboard : AppScreen()
    data class MaterialDetail(val materialId: Long) : AppScreen()
    object AudioRecord : AppScreen()
}

class MainActivity : ComponentActivity() {

    private val viewModel: StudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SnapStudyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SnapStudyApp(viewModel: StudyViewModel) {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Dashboard) }
    val selectedMaterial by viewModel.selectedMaterial.collectAsStateWithLifecycle()
    val allMaterials by viewModel.rawMaterials.collectAsStateWithLifecycle()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(TechNavyDark)
    ) {
        val isWideScreen = maxWidth >= 768.dp

        if (isWideScreen) {
            // Adaptive PC / Tablet Mode (Snapdragon HP PC Landscape)
            when (currentScreen) {
                is AppScreen.AudioRecord -> {
                    BackHandler { currentScreen = AppScreen.Dashboard }
                    AudioRecordScreen(
                        viewModel = viewModel,
                        onNavigateBack = { currentScreen = AppScreen.Dashboard },
                        onProcessingFinished = { currentScreen = AppScreen.Dashboard }
                    )
                }
                else -> {
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Left Pane: Dashboard List & Controls
                        Box(
                            modifier = Modifier
                                .width(380.dp)
                                .fillMaxHeight()
                        ) {
                            DashboardScreen(
                                viewModel = viewModel,
                                onSelectMaterial = { mat ->
                                    viewModel.selectMaterial(mat.id)
                                    currentScreen = AppScreen.MaterialDetail(mat.id)
                                },
                                onNavigateToAudioRecord = {
                                    currentScreen = AppScreen.AudioRecord
                                }
                            )
                        }

                        // Vertical Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(TechNavyBorder)
                        )

                        // Right Pane: Detail View or Placeholder
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            val activeMat = selectedMaterial ?: allMaterials.firstOrNull()
                            if (activeMat != null) {
                                MaterialDetailScreen(
                                    viewModel = viewModel,
                                    material = activeMat,
                                    onNavigateBack = { /* In dual-pane mode, stays active */ }
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Compact Phone / Portrait Mode
            when (val screen = currentScreen) {
                is AppScreen.Dashboard -> {
                    DashboardScreen(
                        viewModel = viewModel,
                        onSelectMaterial = { mat ->
                            viewModel.selectMaterial(mat.id)
                            currentScreen = AppScreen.MaterialDetail(mat.id)
                        },
                        onNavigateToAudioRecord = {
                            currentScreen = AppScreen.AudioRecord
                        }
                    )
                }

                is AppScreen.MaterialDetail -> {
                    BackHandler {
                        currentScreen = AppScreen.Dashboard
                    }
                    val mat = selectedMaterial ?: allMaterials.firstOrNull { it.id == screen.materialId }
                    if (mat != null) {
                        MaterialDetailScreen(
                            viewModel = viewModel,
                            material = mat,
                            onNavigateBack = { currentScreen = AppScreen.Dashboard }
                        )
                    } else {
                        currentScreen = AppScreen.Dashboard
                    }
                }

                is AppScreen.AudioRecord -> {
                    BackHandler {
                        currentScreen = AppScreen.Dashboard
                    }
                    AudioRecordScreen(
                        viewModel = viewModel,
                        onNavigateBack = { currentScreen = AppScreen.Dashboard },
                        onProcessingFinished = {
                            currentScreen = AppScreen.Dashboard
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}

