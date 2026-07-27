package com.example.dailroundsmarrowassessment1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions WHERE questionsUrl = :questionsUrl ORDER BY position")
    suspend fun getByUrl(questionsUrl: String): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(questions: List<QuestionEntity>)
}
