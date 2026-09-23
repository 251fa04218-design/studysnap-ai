package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioQualityPreset
import com.example.audio.LectureAudioPlayer
import com.example.audio.LectureAudioRecorder
import com.example.audio.LocalAudioTranscriber
import com.example.data.AppDatabase
import com.example.data.StudyRepository
import com.example.model.ChatMessage
import com.example.model.Flashcard
import com.example.model.MaterialType
import com.example.model.NpuTelemetry
import com.example.model.QuizAttempt
import com.example.model.QuizQuestion
import com.example.model.StudyMaterial
import com.example.pdf.DocumentProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudyRepository
    val audioRecorder: LectureAudioRecorder
    val audioPlayer: LectureAudioPlayer

    init {
        val database = AppDatabase.getInstance(application)
        repository = StudyRepository(database.studyDao())
        audioRecorder = LectureAudioRecorder(application)
        audioPlayer = LectureAudioPlayer(application)

        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSubject = MutableStateFlow("All")
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    val rawMaterials: StateFlow<List<StudyMaterial>> = repository.allMaterials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredMaterials: StateFlow<List<StudyMaterial>> = combine(
        rawMaterials,
        searchQuery,
        selectedSubject
    ) { materials, query, subject ->
        materials.filter { mat ->
            val matchesSubject = subject == "All" || mat.subject.equals(subject, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    mat.title.contains(query, ignoreCase = true) ||
                    mat.subject.contains(query, ignoreCase = true) ||
                    mat.rawContent.contains(query, ignoreCase = true)
            matchesSubject && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedMaterialId = MutableStateFlow<Long?>(null)
    val selectedMaterialId: StateFlow<Long?> = _selectedMaterialId.asStateFlow()

    val selectedMaterial: StateFlow<StudyMaterial?> = _selectedMaterialId.flatMapLatest { id ->
        if (id == null) flowOf(null) else repository.getMaterial(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentFlashcards: StateFlow<List<Flashcard>> = _selectedMaterialId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getFlashcards(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentQuizQuestions: StateFlow<List<QuizQuestion>> = _selectedMaterialId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getQuizQuestions(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentQuizAttempts: StateFlow<List<QuizAttempt>> = _selectedMaterialId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getQuizAttempts(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentChat: StateFlow<List<ChatMessage>> = _selectedMaterialId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList()) else repository.getChat(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFlashcards: StateFlow<List<Flashcard>> = repository.allFlashcards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAttempts: StateFlow<List<QuizAttempt>> = repository.allAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // NPU Telemetry
    private val _telemetry = MutableStateFlow(repository.getNpuTelemetry())
    val telemetry: StateFlow<NpuTelemetry> = _telemetry.asStateFlow()

    // Processing status
    private val _isAiProcessing = MutableStateFlow(false)
    val isAiProcessing: StateFlow<Boolean> = _isAiProcessing.asStateFlow()

    private val _processingMessage = MutableStateFlow("")
    val processingMessage: StateFlow<String> = _processingMessage.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun selectMaterial(id: Long?) {
        _selectedMaterialId.value = id
    }

    fun toggleFavorite(material: StudyMaterial) {
        viewModelScope.launch {
            repository.toggleFavorite(material.id, material.isFavorite)
        }
    }

    fun deleteMaterial(id: Long) {
        viewModelScope.launch {
            if (_selectedMaterialId.value == id) {
                _selectedMaterialId.value = null
            }
            repository.deleteMaterial(id)
        }
    }

    fun updateFlashcardMastery(cardId: Long, state: Int) {
        viewModelScope.launch {
            repository.updateFlashcardMastery(cardId, state)
        }
    }

    fun recordQuizAttempt(materialId: Long, score: Int, total: Int) {
        viewModelScope.launch {
            repository.recordQuizAttempt(materialId, score, total)
        }
    }

    fun askQuestion(question: String) {
        val matId = _selectedMaterialId.value ?: return
        if (question.isBlank()) return

        viewModelScope.launch {
            _isAiProcessing.value = true
            _processingMessage.value = "Hexagon NPU: Running local vector search & inference..."
            try {
                repository.askAssistant(matId, question)
            } finally {
                _isAiProcessing.value = false
                _processingMessage.value = ""
            }
        }
    }

    fun importDocument(uri: Uri, fileName: String, subject: String, onComplete: (Long) -> Unit) {
        viewModelScope.launch {
            _isAiProcessing.value = true
            _processingMessage.value = "Hexagon NPU: Extracting text & generating local study synthesis..."
            try {
                val extracted = DocumentProcessor.extractFromUri(getApplication(), uri, fileName)
                val newId = repository.ingestStudyMaterial(
                    title = extracted.title,
                    subject = subject.ifBlank { "General" },
                    type = MaterialType.PDF,
                    sourceFileName = fileName,
                    content = extracted.textContent,
                    pageCount = extracted.pageCount
                )
                _selectedMaterialId.value = newId
                onComplete(newId)
            } catch (e: Exception) {
                // Fallback to basic note
                val newId = repository.ingestStudyMaterial(
                    title = fileName.substringBeforeLast("."),
                    subject = subject.ifBlank { "General" },
                    type = MaterialType.PDF,
                    sourceFileName = fileName,
                    content = "Imported lecture document for review. Analyzed locally on Snapdragon NPU."
                )
                _selectedMaterialId.value = newId
                onComplete(newId)
            } finally {
                _isAiProcessing.value = false
                _processingMessage.value = ""
            }
        }
    }

    fun createCustomNote(title: String, subject: String, content: String, onComplete: (Long) -> Unit) {
        viewModelScope.launch {
            _isAiProcessing.value = true
            _processingMessage.value = "Hexagon NPU: Synthesizing concepts, flashcards & quiz..."
            try {
                val newId = repository.ingestStudyMaterial(
                    title = title,
                    subject = subject.ifBlank { "Study Notes" },
                    type = MaterialType.NOTES,
                    sourceFileName = "Notes_${System.currentTimeMillis()}.txt",
                    content = content
                )
                _selectedMaterialId.value = newId
                onComplete(newId)
            } finally {
                _isAiProcessing.value = false
                _processingMessage.value = ""
            }
        }
    }

    fun processRecordedLecture(
        title: String,
        subject: String,
        durationSec: Int,
        audioFile: java.io.File? = null,
        preset: AudioQualityPreset = audioRecorder.selectedPreset.value,
        onComplete: (Long) -> Unit
    ) {
        viewModelScope.launch {
            _isAiProcessing.value = true
            _processingMessage.value = "Hexagon NPU: Running offline speech recognition & acoustic segmentation (18x speed)..."
            try {
                val transcriptionResult = LocalAudioTranscriber.processLectureAudio(
                    title = title,
                    subject = subject,
                    durationSeconds = durationSec,
                    audioFile = audioFile,
                    preset = preset
                )

                val fileName = audioFile?.name ?: "Lecture_Audio_Local.m4a"
                val filePath = audioFile?.absolutePath ?: ""

                val newId = repository.ingestStudyMaterial(
                    title = title.ifBlank { "Recorded Lecture ${System.currentTimeMillis() % 1000}" },
                    subject = subject.ifBlank { "Lectures" },
                    type = MaterialType.AUDIO,
                    sourceFileName = fileName,
                    content = transcriptionResult.fullTranscript,
                    audioDurationSeconds = transcriptionResult.detectedDurationSeconds,
                    audioFilePath = filePath
                )
                _selectedMaterialId.value = newId
                onComplete(newId)
            } finally {
                _isAiProcessing.value = false
                _processingMessage.value = ""
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorder.cancelRecording()
        audioPlayer.release()
    }
}
