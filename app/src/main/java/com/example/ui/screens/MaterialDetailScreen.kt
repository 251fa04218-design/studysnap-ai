package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ChatMessage
import com.example.model.Flashcard
import com.example.model.MaterialType
import com.example.model.QuizQuestion
import com.example.model.StudyMaterial
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
import org.json.JSONArray

@Composable
fun MaterialDetailScreen(
    viewModel: StudyViewModel,
    material: StudyMaterial,
    onNavigateBack: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("AI Summary", "Ask AI (Q&A)", "Flashcards", "Practice Quiz", "Source Text")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = TechNavyDark,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TechNavyCard)
            ) {
                // Top App Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.testTag("detail_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Column(modifier = Modifier.padding(start = 4.dp)) {
                            Text(
                                text = material.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = material.subject,
                                    fontSize = 11.sp,
                                    color = HexagonCyan
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(NpuGreen)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Snapdragon NPU Active",
                                    fontSize = 11.sp,
                                    color = NpuGreen
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = { viewModel.toggleFavorite(material) },
                        modifier = Modifier.testTag("detail_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (material.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle Favorite",
                            tint = if (material.isFavorite) SnapdragonRedBright else TextSecondaryDark
                        )
                    }
                }

                // Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = TechNavyCard,
                    contentColor = SnapdragonRedBright,
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = SnapdragonRedBright,
                            height = 3.dp
                        )
                    },
                    divider = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(TechNavyBorder)
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (selectedTabIndex == index) Color.White else TextSecondaryDark
                                )
                            },
                            modifier = Modifier.testTag("tab_$index")
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTabIndex) {
                0 -> SummaryTab(viewModel, material)
                1 -> ChatTab(viewModel, material)
                2 -> FlashcardsTab(viewModel, material)
                3 -> QuizTab(viewModel, material)
                4 -> SourceTextTab(viewModel, material)
            }
        }
    }
}

@Composable
private fun SummaryTab(viewModel: StudyViewModel, material: StudyMaterial) {
    val keyConcepts = remember(material.keyConceptsJson) {
        try {
            val arr = JSONArray(material.keyConceptsJson)
            (0 until arr.length()).map { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    val keyFormulas = remember(material.keyFormulasJson) {
        try {
            val arr = JSONArray(material.keyFormulasJson)
            (0 until arr.length()).map { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(6.dp)) }

        // Local Processing Shield Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = TechNavyCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SnapdragonRed.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = SnapdragonRedBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Synthesized Locally on Hexagon NPU",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Latency: ${material.npuProcessingTimeMs}ms • 0 bytes transmitted externally",
                            fontSize = 11.sp,
                            color = HexagonCyan
                        )
                    }
                }
            }
        }

        // Dedicated Audio Player if this is an audio lecture
        if (material.type == MaterialType.AUDIO || material.audioDurationSeconds > 0) {
            item {
                LectureAudioPlayerCard(viewModel, material)
            }
        }

        // Executive Summary
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = TechNavyCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SnapdragonRedBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Executive Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = material.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE2E8F0),
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Key Concepts
        if (keyConcepts.isNotEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = TechNavyCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "High-Yield Concepts",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            keyConcepts.forEach { concept ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(TechNavySurface)
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(HexagonCyan)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = concept,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Formulas & Rules
        if (keyFormulas.isNotEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = TechNavyCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Functions,
                                contentDescription = null,
                                tint = HexagonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Formulas & Governing Principles",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            keyFormulas.forEach { formula ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(TechNavySurface)
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = formula,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = HexagonCyan,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun ChatTab(viewModel: StudyViewModel, material: StudyMaterial) {
    val messages by viewModel.currentChat.collectAsStateWithLifecycle()
    val isAiProcessing by viewModel.isAiProcessing.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickQuestions = listOf(
        "Summarize core thesis in 3 bullets",
        "What are potential exam questions?",
        "Explain key terms simply",
        "How does local processing work?"
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Assistant Welcome Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = TechNavyCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SnapdragonRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Snapdragon Local Study Assistant",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Ask any question about \"${material.title}\". Answers are retrieved with semantic citation directly from your notes using the on-device Hexagon NPU.",
                            fontSize = 12.sp,
                            color = TextSecondaryDark,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Message Items
            items(messages) { msg ->
                ChatMessageBubble(msg)
            }

            if (isAiProcessing) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = HexagonCyan,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Hexagon NPU: Quantized vector search...",
                            fontSize = 12.sp,
                            color = HexagonCyan
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        // Quick Suggestion Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { q ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = TechNavyCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder),
                    modifier = Modifier.clickable {
                        viewModel.askQuestion(q)
                    }
                ) {
                    Text(
                        text = q,
                        fontSize = 11.sp,
                        color = HexagonCyan,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Input Field
        Surface(
            color = TechNavyCard,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask a question about this material...", fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    maxLines = 3,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SnapdragonRedBright,
                        unfocusedBorderColor = TechNavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val textToSend = inputText
                            inputText = ""
                            viewModel.askQuestion(textToSend)
                        }
                    },
                    enabled = inputText.isNotBlank(),
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) SnapdragonRed else TechNavySurface)
                        .testTag("send_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank()) Color.White else TextSecondaryDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(msg: ChatMessage) {
    val isUser = msg.isUser
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            color = if (isUser) SnapdragonRed else TechNavyCard,
            border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder),
            modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.95f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = msg.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    lineHeight = 20.sp
                )

                if (!isUser && msg.citation.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(TechNavySurface)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "📎 ${msg.citation}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = HexagonCyan
                        )
                    }
                }

                if (!isUser && msg.inferenceLatencyMs > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = NpuGreen,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${msg.inferenceLatencyMs}ms • ${msg.tokensPerSec} tok/s • Hexagon NPU",
                            fontSize = 10.sp,
                            color = NpuGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardsTab(viewModel: StudyViewModel, material: StudyMaterial) {
    val flashcards by viewModel.currentFlashcards.collectAsStateWithLifecycle()
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "cardFlip"
    )

    if (flashcards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No flashcards generated yet for this material.",
                color = TextSecondaryDark,
                fontSize = 14.sp
            )
        }
        return
    }

    val currentCard = flashcards[currentIndex.coerceIn(0, flashcards.size - 1)]
    val masteredCount = flashcards.count { it.masteryState == 2 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Card ${currentIndex + 1} of ${flashcards.size}",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )

            Text(
                text = "Mastered: $masteredCount/${flashcards.size}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AmberGold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3D Flip Flashcard
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clip(RoundedCornerShape(20.dp))
                .border(
                    1.dp,
                    Brush.linearGradient(
                        listOf(SnapdragonRed.copy(alpha = 0.6f), HexagonCyan.copy(alpha = 0.4f))
                    ),
                    RoundedCornerShape(20.dp)
                )
                .clickable { isFlipped = !isFlipped }
                .testTag("flashcard_flip_card"),
            color = TechNavyCard,
            tonalElevation = 6.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (rotation <= 90f) {
                    // Front: Question / Term
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SnapdragonRed.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "CONCEPT / TERM",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SnapdragonRedBright
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = currentCard.term,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraAndroid,
                                contentDescription = null,
                                tint = TextSecondaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tap anywhere to flip",
                                fontSize = 12.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }
                } else {
                    // Back: Definition & Context (flipped graphicsLayer correction)
                    Column(
                        modifier = Modifier.graphicsLayer { rotationY = 180f },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(HexagonCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "EXPLANATION & DEFINITION",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = HexagonCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = currentCard.definition,
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )

                        if (currentCard.contextSource.isNotBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Source: ${currentCard.contextSource}",
                                fontSize = 11.sp,
                                color = HexagonCyan
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mastery Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    viewModel.updateFlashcardMastery(currentCard.id, 1)
                    isFlipped = false
                    if (currentIndex < flashcards.size - 1) currentIndex++ else currentIndex = 0
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("flashcard_review_button"),
                colors = ButtonDefaults.buttonColors(containerColor = TechNavySurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Need Review", color = Color(0xFFEF5350), fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = {
                    viewModel.updateFlashcardMastery(currentCard.id, 2)
                    isFlipped = false
                    if (currentIndex < flashcards.size - 1) currentIndex++ else currentIndex = 0
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("flashcard_mastered_button"),
                colors = ButtonDefaults.buttonColors(containerColor = NpuGreen.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Mastered ✓", color = Color(0xFF003314), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun QuizTab(viewModel: StudyViewModel, material: StudyMaterial) {
    val quizQuestions by viewModel.currentQuizQuestions.collectAsStateWithLifecycle()
    var currentQIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }

    if (quizQuestions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No practice quiz generated for this document.",
                color = TextSecondaryDark,
                fontSize = 14.sp
            )
        }
        return
    }

    if (isCompleted) {
        // Quiz Results Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(SnapdragonRed.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = SnapdragonRedBright,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Practice Quiz Complete!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Score: $score / ${quizQuestions.size}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (score >= quizQuestions.size / 2) NpuGreen else AmberGold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Assessed locally via Snapdragon Hexagon NPU with 0 cloud data transfer.",
                fontSize = 12.sp,
                color = HexagonCyan,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    currentQIndex = 0
                    selectedOption = null
                    isSubmitted = false
                    score = 0
                    isCompleted = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("retake_quiz_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SnapdragonRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retake Quiz", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        return
    }

    val q = quizQuestions[currentQIndex.coerceIn(0, quizQuestions.size - 1)]
    val options = remember(q.optionsJson) {
        try {
            val arr = JSONArray(q.optionsJson)
            (0 until arr.length()).map { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentQIndex + 1} of ${quizQuestions.size}",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = "Score: $score",
                    fontWeight = FontWeight.SemiBold,
                    color = HexagonCyan,
                    fontSize = 13.sp
                )
            }
        }

        // Question card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = TechNavyCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Options
        items(options.indices.toList()) { index ->
            val optText = options[index]
            val isSelected = selectedOption == index
            val isCorrect = index == q.correctOptionIndex

            val backgroundColor = when {
                !isSubmitted && isSelected -> SnapdragonRed.copy(alpha = 0.25f)
                isSubmitted && isCorrect -> NpuGreen.copy(alpha = 0.2f)
                isSubmitted && isSelected && !isCorrect -> Color(0xFFEF5350).copy(alpha = 0.2f)
                else -> TechNavyCard
            }

            val borderColor = when {
                !isSubmitted && isSelected -> SnapdragonRedBright
                isSubmitted && isCorrect -> NpuGreen
                isSubmitted && isSelected && !isCorrect -> Color(0xFFEF5350)
                else -> TechNavyBorder
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                    .clickable(enabled = !isSubmitted) {
                        selectedOption = index
                    }
                    .testTag("quiz_option_$index"),
                color = backgroundColor
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) SnapdragonRedBright else TechNavySurface
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ('A'.code + index).toChar().toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = optText,
                        fontSize = 13.sp,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Explanation & Next Button
        if (isSubmitted) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = TechNavySurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (selectedOption == q.correctOptionIndex) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (selectedOption == q.correctOptionIndex) NpuGreen else Color(0xFFEF5350),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (selectedOption == q.correctOptionIndex) "Correct!" else "Incorrect",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOption == q.correctOptionIndex) NpuGreen else Color(0xFFEF5350),
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = q.explanation,
                            fontSize = 12.sp,
                            color = TextSecondaryDark,
                            lineHeight = 18.sp
                        )

                        if (q.citation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Source: ${q.citation}",
                                fontSize = 11.sp,
                                color = HexagonCyan
                            )
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    if (!isSubmitted) {
                        if (selectedOption != null) {
                            isSubmitted = true
                            if (selectedOption == q.correctOptionIndex) {
                                score++
                            }
                        }
                    } else {
                        if (currentQIndex < quizQuestions.size - 1) {
                            currentQIndex++
                            selectedOption = null
                            isSubmitted = false
                        } else {
                            viewModel.recordQuizAttempt(material.id, score, quizQuestions.size)
                            isCompleted = true
                        }
                    }
                },
                enabled = selectedOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("quiz_action_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SnapdragonRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (!isSubmitted) "Submit Answer" else if (currentQIndex < quizQuestions.size - 1) "Next Question →" else "Finish Quiz",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SourceTextTab(viewModel: StudyViewModel, material: StudyMaterial) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (material.type == MaterialType.AUDIO || material.audioDurationSeconds > 0) {
            item {
                LectureAudioPlayerCard(viewModel, material)
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = TechNavyCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Source File: ${material.sourceFileName}",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (material.type == MaterialType.AUDIO) "Snapdragon Hexagon DSP: Real-Time Acoustic Speech Transcript" else "Indexed into local vector chunks for Snapdragon NPU RAG retrieval",
                            fontSize = 11.sp,
                            color = HexagonCyan
                        )
                    }
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = TechNavyCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, TechNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = material.rawContent,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 22.sp
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun LectureAudioPlayerCard(
    viewModel: StudyViewModel,
    material: StudyMaterial,
    modifier: Modifier = Modifier
) {
    val player = viewModel.audioPlayer
    val isPlaying by player.isPlaying.collectAsStateWithLifecycle()
    val currentPosMs by player.currentPositionMs.collectAsStateWithLifecycle()
    val totalDurationMs by player.durationMs.collectAsStateWithLifecycle()
    val speed by player.playbackSpeed.collectAsStateWithLifecycle()
    val activePath by player.activeFilePath.collectAsStateWithLifecycle()

    val effectiveDurationSec = if (material.audioDurationSeconds > 0) material.audioDurationSeconds else 300
    val totalMs = if (totalDurationMs > 0 && activePath == material.audioFilePath) totalDurationMs else (effectiveDurationSec * 1000)

    val currentSeconds = (currentPosMs / 1000).coerceAtMost(effectiveDurationSec)
    val progress = if (totalMs > 0) (currentPosMs.toFloat() / totalMs).coerceIn(0f, 1f) else 0f

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = TechNavyCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SnapdragonRed.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SnapdragonRed.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = SnapdragonRedBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Lecture Audio Recording",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "High-Quality MediaRecorder AAC • 0 KB Cloud Upload",
                            fontSize = 11.sp,
                            color = HexagonCyan
                        )
                    }
                }

                // Speed Selector
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(1.0f, 1.25f, 1.5f, 2.0f).forEach { s ->
                        val isSelected = (speed == s)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) SnapdragonRed else TechNavySurface)
                                .clickable { player.setPlaybackSpeed(s) }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${s}x",
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondaryDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Seek slider
            Slider(
                value = progress,
                onValueChange = { newProgress ->
                    val targetMs = (newProgress * totalMs).toInt()
                    player.seekTo(targetMs)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("audio_player_seek_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = SnapdragonRedBright,
                    activeTrackColor = SnapdragonRedBright,
                    inactiveTrackColor = TechNavyBorder
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60),
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = String.format("%02d:%02d", effectiveDurationSec / 60, effectiveDurationSec % 60),
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Play / Pause Button Center
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val file = if (material.audioFilePath.isNotBlank()) java.io.File(material.audioFilePath) else null
                        if (file != null && file.exists()) {
                            if (activePath == material.audioFilePath) {
                                player.togglePlayPause()
                            } else {
                                player.loadAndPlay(material.audioFilePath)
                            }
                        } else {
                            player.togglePlayPause()
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(SnapdragonRed)
                        .testTag("audio_player_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

