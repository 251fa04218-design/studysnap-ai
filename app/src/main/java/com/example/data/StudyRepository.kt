package com.example.data

import android.content.Context
import com.example.ai.OnDeviceAIEngine
import com.example.model.ChatMessage
import com.example.model.Flashcard
import com.example.model.MaterialType
import com.example.model.NpuTelemetry
import com.example.model.QuizAttempt
import com.example.model.QuizQuestion
import com.example.model.StudyMaterial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.json.JSONArray

class StudyRepository(private val dao: StudyDao) {

    val allMaterials: Flow<List<StudyMaterial>> = dao.getAllMaterials()
    val allFlashcards: Flow<List<Flashcard>> = dao.getAllFlashcards()
    val allAttempts: Flow<List<QuizAttempt>> = dao.getAllAttempts()

    fun getMaterial(id: Long): Flow<StudyMaterial?> = dao.getMaterialById(id)
    fun getFlashcards(materialId: Long): Flow<List<Flashcard>> = dao.getFlashcardsForMaterial(materialId)
    fun getQuizQuestions(materialId: Long): Flow<List<QuizQuestion>> = dao.getQuizQuestionsForMaterial(materialId)
    fun getQuizAttempts(materialId: Long): Flow<List<QuizAttempt>> = dao.getAttemptsForMaterial(materialId)
    fun getChat(materialId: Long): Flow<List<ChatMessage>> = dao.getChatMessages(materialId)

    suspend fun checkAndSeedInitialData() {
        val existing = dao.getAllMaterials().firstOrNull()
        if (existing.isNullOrEmpty()) {
            val csId = dao.insertMaterial(SampleData.csMaterial)
            val bioId = dao.insertMaterial(SampleData.bioMaterial)
            val audioId = dao.insertMaterial(SampleData.audioLectureMaterial)

            dao.insertFlashcards(SampleData.getSampleFlashcards(csId))
            dao.insertFlashcards(SampleData.getSampleFlashcards(bioId))
            dao.insertFlashcards(SampleData.getSampleFlashcards(audioId))

            dao.insertQuizQuestions(SampleData.getSampleQuiz(csId))
            dao.insertQuizQuestions(SampleData.getSampleQuiz(bioId))
            dao.insertQuizQuestions(SampleData.getSampleQuiz(audioId))

            SampleData.getSampleChat(csId).forEach { dao.insertChatMessage(it.copy(materialId = csId)) }
            SampleData.getSampleChat(audioId).forEach { dao.insertChatMessage(it.copy(materialId = audioId)) }
        }
    }

    suspend fun toggleFavorite(id: Long, currentVal: Boolean) {
        dao.setFavorite(id, !currentVal)
    }

    suspend fun deleteMaterial(id: Long) {
        val mat = dao.getMaterialByIdDirect(id)
        if (mat != null && mat.audioFilePath.isNotBlank()) {
            try {
                val f = java.io.File(mat.audioFilePath)
                if (f.exists()) f.delete()
            } catch (e: Exception) {
                // ignore
            }
        }
        dao.deleteMaterialById(id)
        dao.deleteFlashcardsForMaterial(id)
        dao.deleteQuizQuestionsForMaterial(id)
        dao.clearChat(id)
    }

    suspend fun updateFlashcardMastery(id: Long, newState: Int) {
        dao.updateFlashcardMastery(id, newState, System.currentTimeMillis())
    }

    suspend fun recordQuizAttempt(materialId: Long, score: Int, total: Int) {
        dao.insertAttempt(QuizAttempt(materialId = materialId, score = score, totalQuestions = total))
    }

    suspend fun askAssistant(materialId: Long, question: String): ChatMessage {
        val material = dao.getMaterialByIdDirect(materialId)
            ?: return ChatMessage(materialId = materialId, isUser = false, message = "Document not found in local storage.")

        // Store user query
        val userMsg = ChatMessage(
            materialId = materialId,
            isUser = true,
            message = question,
            timestamp = System.currentTimeMillis()
        )
        dao.insertChatMessage(userMsg)

        // Process locally via Hexagon NPU engine
        val aiResponse = OnDeviceAIEngine.answerQuestion(
            documentTitle = material.title,
            documentContent = material.rawContent,
            question = question
        ).copy(materialId = materialId)

        dao.insertChatMessage(aiResponse)
        return aiResponse
    }

    /**
     * Ingests a new document or recorded audio lecture completely locally,
     * extracts text, generates summaries, flashcards, and quizzes using the on-device AI engine.
     */
    suspend fun ingestStudyMaterial(
        title: String,
        subject: String,
        type: MaterialType,
        sourceFileName: String,
        content: String,
        audioDurationSeconds: Int = 0,
        audioFilePath: String = "",
        pageCount: Int = 1
    ): Long {
        val startTime = System.currentTimeMillis()
        val (summary, keyConcepts, keyFormulas) = OnDeviceAIEngine.generateSummary(title, content)
        val elapsed = System.currentTimeMillis() - startTime

        val material = StudyMaterial(
            title = title,
            subject = subject,
            type = type,
            sourceFileName = sourceFileName,
            rawContent = content,
            summary = summary,
            keyConceptsJson = JSONArray(keyConcepts).toString(),
            keyFormulasJson = JSONArray(keyFormulas).toString(),
            audioDurationSeconds = audioDurationSeconds,
            audioFilePath = audioFilePath,
            pageCount = pageCount,
            npuProcessingTimeMs = elapsed,
            tokenCount = (content.length / 4).coerceAtLeast(100)
        )

        val newId = dao.insertMaterial(material)

        // Auto-generate flashcards & quiz questions with the on-device AI
        val flashcards = OnDeviceAIEngine.generateFlashcards(newId, content)
        if (flashcards.isNotEmpty()) {
            dao.insertFlashcards(flashcards)
        }

        val quiz = OnDeviceAIEngine.generateQuiz(newId, content, title)
        if (quiz.isNotEmpty()) {
            dao.insertQuizQuestions(quiz)
        }

        return newId
    }

    fun getNpuTelemetry(): NpuTelemetry {
        return OnDeviceAIEngine.getTelemetry()
    }
}
