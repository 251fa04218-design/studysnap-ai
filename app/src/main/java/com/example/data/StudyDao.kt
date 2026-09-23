package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.ChatMessage
import com.example.model.Flashcard
import com.example.model.QuizAttempt
import com.example.model.QuizQuestion
import com.example.model.StudyMaterial
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {

    // Study Materials
    @Query("SELECT * FROM study_materials ORDER BY createdTimestamp DESC")
    fun getAllMaterials(): Flow<List<StudyMaterial>>

    @Query("SELECT * FROM study_materials WHERE id = :id")
    fun getMaterialById(id: Long): Flow<StudyMaterial?>

    @Query("SELECT * FROM study_materials WHERE id = :id")
    suspend fun getMaterialByIdDirect(id: Long): StudyMaterial?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: StudyMaterial): Long

    @Update
    suspend fun updateMaterial(material: StudyMaterial)

    @Query("DELETE FROM study_materials WHERE id = :id")
    suspend fun deleteMaterialById(id: Long)

    @Query("UPDATE study_materials SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    // Flashcards
    @Query("SELECT * FROM flashcards WHERE materialId = :materialId ORDER BY id ASC")
    fun getFlashcardsForMaterial(materialId: Long): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards ORDER BY id ASC")
    fun getAllFlashcards(): Flow<List<Flashcard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: Flashcard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<Flashcard>)

    @Query("UPDATE flashcards SET masteryState = :state, lastReviewedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateFlashcardMastery(id: Long, state: Int, timestamp: Long)

    @Query("DELETE FROM flashcards WHERE materialId = :materialId")
    suspend fun deleteFlashcardsForMaterial(materialId: Long)

    // Quizzes
    @Query("SELECT * FROM quiz_questions WHERE materialId = :materialId ORDER BY id ASC")
    fun getQuizQuestionsForMaterial(materialId: Long): Flow<List<QuizQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(questions: List<QuizQuestion>)

    @Query("DELETE FROM quiz_questions WHERE materialId = :materialId")
    suspend fun deleteQuizQuestionsForMaterial(materialId: Long)

    // Quiz Attempts
    @Query("SELECT * FROM quiz_attempts WHERE materialId = :materialId ORDER BY timestamp DESC")
    fun getAttemptsForMaterial(materialId: Long): Flow<List<QuizAttempt>>

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<QuizAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttempt): Long

    // Chat
    @Query("SELECT * FROM chat_messages WHERE materialId = :materialId ORDER BY timestamp ASC")
    fun getChatMessages(materialId: Long): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage): Long

    @Query("DELETE FROM chat_messages WHERE materialId = :materialId")
    suspend fun clearChat(materialId: Long)
}
