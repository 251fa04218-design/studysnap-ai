package com.example.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.StudyMaterial
import com.example.ui.components.HardwareTelemetryHeader
import com.example.ui.components.StudyMaterialCard
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
fun DashboardScreen(
    viewModel: StudyViewModel,
    onSelectMaterial: (StudyMaterial) -> Unit,
    onNavigateToAudioRecord: () -> Unit
) {
    val materials by viewModel.filteredMaterials.collectAsStateWithLifecycle()
    val allFlashcards by viewModel.allFlashcards.collectAsStateWithLifecycle()
    val telemetry by viewModel.telemetry.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val isAiProcessing by viewModel.isAiProcessing.collectAsStateWithLifecycle()
    val processingMessage by viewModel.processingMessage.collectAsStateWithLifecycle()

    var showHardwareDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showFabMenu by remember { mutableStateOf(false) }

    val subjects = listOf("All", "Computer Science", "Biology", "Electrical Engineering", "General")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = TechNavyDark,
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (showFabMenu) {
                    // Quick Action: Record Lecture Audio
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = TechNavyCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder),
                        modifier = Modifier
                            .clickable {
                                showFabMenu = false
                                onNavigateToAudioRecord()
                            }
                            .testTag("fab_record_audio")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = SnapdragonRedBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Record Classroom Lecture",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Quick Action: Import PDF or Notes
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = TechNavyCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder),
                        modifier = Modifier
                            .clickable {
                                showFabMenu = false
                                showImportDialog = true
                            }
                            .testTag("fab_import_pdf")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = null,
                                tint = HexagonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Import PDF / Paste Notes",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { showFabMenu = !showFabMenu },
                    containerColor = SnapdragonRed,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("dashboard_main_fab")
                ) {
                    Icon(
                        imageVector = if (showFabMenu) Icons.Default.Add else Icons.Default.Add,
                        contentDescription = "Add Material"
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Hero Brand Banner with Image
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, TechNavyBorder, RoundedCornerShape(20.dp)),
                    color = TechNavyCard
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_hero_study),
                                contentDescription = "Snapdragon HP PC Workspace",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, TechNavyCard)
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SnapdragonRed.copy(alpha = 0.9f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "OFFLINE AI • 100% PRIVATE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "SnapStudy AI",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "On-Device Study Assistant for Snapdragon® HP PCs",
                                style = MaterialTheme.typography.bodyMedium,
                                color = HexagonCyan
                            )
                        }
                    }
                }
            }

            // Live Snapdragon NPU Telemetry Header
            item {
                HardwareTelemetryHeader(
                    telemetry = telemetry,
                    onOpenDetails = { showHardwareDialog = true }
                )
            }

            // Processing Banner (if AI is synthesizing)
            if (isAiProcessing) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = TechNavySurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HexagonCyan.copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = SnapdragonRedBright,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = processingMessage.ifBlank { "Hexagon NPU: Processing on-device neural model..." },
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Study Overview Metric Strip
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val mastered = allFlashcards.count { it.masteryState == 2 }
                    val totalCards = allFlashcards.size

                    QuickStatTile(
                        label = "Course Files",
                        value = "${materials.size}",
                        subtext = "Indexed Locally",
                        icon = Icons.Default.Description,
                        tint = SnapdragonRedBright,
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatTile(
                        label = "Flashcards",
                        value = "$mastered / $totalCards",
                        subtext = "Mastered",
                        icon = Icons.Default.School,
                        tint = AmberGold,
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatTile(
                        label = "Data Leakage",
                        value = "0 KB",
                        subtext = "Air-Gapped",
                        icon = Icons.Default.Security,
                        tint = NpuGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search lecture notes, terms, formulas...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextSecondaryDark
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dashboard_search_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SnapdragonRedBright,
                        unfocusedBorderColor = TechNavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // Subject Filter Pills
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(subjects) { subj ->
                        val isSelected = subj.equals(selectedSubject, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) SnapdragonRed else TechNavyCard,
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder),
                            modifier = Modifier
                                .clickable { viewModel.setSelectedSubject(subj) }
                                .testTag("subject_filter_$subj")
                        ) {
                            Text(
                                text = subj,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondaryDark,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "My Study Materials (${materials.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TechNavySurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder),
                            modifier = Modifier
                                .clickable { onNavigateToAudioRecord() }
                                .testTag("quick_record_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = SnapdragonRedBright,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Record", fontSize = 11.sp, color = Color.White)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TechNavySurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder),
                            modifier = Modifier
                                .clickable { showImportDialog = true }
                                .testTag("quick_import_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = HexagonCyan,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Materials List or Empty State
            if (materials.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = TechNavyCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = TextSecondaryDark,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No course materials found",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Import a PDF, paste notes, or record classroom audio to synthesize study guides on the Snapdragon Hexagon NPU.",
                                color = TextSecondaryDark,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else {
                items(materials, key = { it.id }) { mat ->
                    StudyMaterialCard(
                        material = mat,
                        onClick = { onSelectMaterial(mat) },
                        onToggleFavorite = { viewModel.toggleFavorite(mat) },
                        onDelete = { viewModel.deleteMaterial(mat.id) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(70.dp)) }
        }
    }

    // Dialogs
    if (showHardwareDialog) {
        HardwareNpuDialog(
            telemetry = telemetry,
            onDismiss = { showHardwareDialog = false }
        )
    }

    if (showImportDialog) {
        ImportDialog(
            onDismiss = { showImportDialog = false },
            onImportPdf = { uri, fileName, subject ->
                viewModel.importDocument(uri, fileName, subject) { newId ->
                    val created = viewModel.rawMaterials.value.firstOrNull { it.id == newId }
                    if (created != null) onSelectMaterial(created)
                }
            },
            onCreateNote = { title, subject, content ->
                viewModel.createCustomNote(title, subject, content) { newId ->
                    val created = viewModel.rawMaterials.value.firstOrNull { it.id == newId }
                    if (created != null) onSelectMaterial(created)
                }
            }
        )
    }
}

@Composable
private fun QuickStatTile(
    label: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, TechNavyBorder, RoundedCornerShape(12.dp)),
        color = TechNavyCard
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label, fontSize = 10.sp, color = TextSecondaryDark)
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(13.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtext,
                fontSize = 9.sp,
                color = tint
            )
        }
    }
}
