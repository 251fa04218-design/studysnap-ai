package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MaterialType {
    PDF,
    AUDIO,
    NOTES
}

@Entity(tableName = "study_materials")
data class StudyMaterial(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subject: String,
    val type: MaterialType,
    val sourceFileName: String,
    val rawContent: String,
    val summary: String,
    val keyConceptsJson: String,
    val keyFormulasJson: String,
    val audioDurationSeconds: Int = 0,
    val audioFilePath: String = "",
    val pageCount: Int = 1,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val npuProcessingTimeMs: Long = 180,
    val tokenCount: Int = 1200
)

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val materialId: Long,
    val term: String,
    val definition: String,
    val contextSource: String = "",
    val masteryState: Int = 0, // 0 = Unstudied, 1 = Reviewing, 2 = Mastered
    val lastReviewedTimestamp: Long = 0
)

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val materialId: Long,
    val question: String,
    val optionsJson: String, // serialized JSON list
    val correctOptionIndex: Int,
    val explanation: String,
    val citation: String
)

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val materialId: Long,
    val score: Int,
    val totalQuestions: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val materialId: Long,
    val isUser: Boolean,
    val message: String,
    val citation: String = "",
    val inferenceLatencyMs: Long = 0,
    val tokensPerSec: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)

data class NpuTelemetry(
    val npuModelName: String = "Qualcomm® Hexagon™ NPU (Snapdragon® X Elite)",
    val peakTops: Double = 45.0,
    val currentTopsUtilized: Double = 12.4,
    val localTokensPerSec: Float = 44.8f,
    val avgLatencyMs: Long = 18,
    val powerEfficiencyWatts: Float = 2.8f,
    val bytesUploadedToCloud: Long = 0L, // Always 0 (Privacy Shield)
    val isAirGapped: Boolean = true
)
