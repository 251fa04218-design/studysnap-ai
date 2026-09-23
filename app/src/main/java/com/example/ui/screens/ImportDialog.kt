package com.example.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.HexagonCyan
import com.example.ui.theme.SnapdragonRed
import com.example.ui.theme.SnapdragonRedBright
import com.example.ui.theme.TechNavyBorder
import com.example.ui.theme.TechNavyCard
import com.example.ui.theme.TechNavySurface
import com.example.ui.theme.TextSecondaryDark

@Composable
fun ImportDialog(
    onDismiss: () -> Unit,
    onImportPdf: (Uri, String, String) -> Unit,
    onCreateNote: (String, String, String) -> Unit
) {
    var mode by remember { mutableStateOf("CHOICE") }
    var noteTitle by remember { mutableStateOf("") }
    var noteSubject by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current

    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = try {
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (cursor.moveToFirst() && nameIndex >= 0) cursor.getString(nameIndex) else null
                }
            } catch (e: Exception) {
                null
            } ?: uri.lastPathSegment ?: "Course_Material_${System.currentTimeMillis()}.pdf"

            onImportPdf(uri, fileName, "Imported Material")
            onDismiss()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, TechNavyBorder, RoundedCornerShape(20.dp))
                .testTag("import_dialog"),
            color = TechNavyCard,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SnapdragonRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (mode == "CHOICE") "Import Study Material" else "Paste Course Notes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondaryDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (mode == "CHOICE") {
                    Text(
                        text = "All documents are processed 100% locally on your Snapdragon Hexagon NPU. Zero cloud transmission.",
                        style = MaterialTheme.typography.bodySmall,
                        color = HexagonCyan
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Option 1: File Picker
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, TechNavyBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                documentPickerLauncher.launch(
                                    arrayOf(
                                        "application/pdf",
                                        "text/plain",
                                        "text/markdown",
                                        "text/html"
                                    )
                                )
                            }
                            .testTag("open_file_picker_button"),
                        color = TechNavySurface
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SnapdragonRedBright.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FileOpen,
                                    contentDescription = null,
                                    tint = SnapdragonRedBright
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Select PDF or Document",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Open syllabus, research paper or textbook",
                                    fontSize = 12.sp,
                                    color = TextSecondaryDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option 2: Paste notes manually
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, TechNavyBorder, RoundedCornerShape(12.dp))
                            .clickable { mode = "MANUAL_NOTE" }
                            .testTag("paste_notes_button"),
                        color = TechNavySurface
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(HexagonCyan.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = HexagonCyan
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Paste Lecture Notes or Text",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Type or paste transcript, formulas, or slides",
                                    fontSize = 12.sp,
                                    color = TextSecondaryDark
                                )
                            }
                        }
                    }
                } else {
                    // Manual Note Entry
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Course / Lecture Title") },
                        placeholder = { Text("e.g. CS 301: Algorithms & Data Structures") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_title_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SnapdragonRedBright,
                            unfocusedBorderColor = TechNavyBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = noteSubject,
                        onValueChange = { noteSubject = it },
                        label = { Text("Subject / Department") },
                        placeholder = { Text("e.g. Computer Science, Physics") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_subject_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SnapdragonRedBright,
                            unfocusedBorderColor = TechNavyBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = noteContent,
                        onValueChange = { noteContent = it },
                        label = { Text("Lecture Content & Notes") },
                        placeholder = { Text("Paste notes, chapter summaries, formulas, or transcript here...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .testTag("note_content_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SnapdragonRedBright,
                            unfocusedBorderColor = TechNavyBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { mode = "CHOICE" },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TechNavySurface),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Back", color = Color.White)
                        }

                        Button(
                            onClick = {
                                if (noteContent.isNotBlank()) {
                                    val title = noteTitle.ifBlank { "Study Note ${System.currentTimeMillis() % 1000}" }
                                    val subject = noteSubject.ifBlank { "General" }
                                    onCreateNote(title, subject, noteContent)
                                    onDismiss()
                                }
                            },
                            enabled = noteContent.isNotBlank(),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(46.dp)
                                .testTag("submit_note_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SnapdragonRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Process on NPU", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
