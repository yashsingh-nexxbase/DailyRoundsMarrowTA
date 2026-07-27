package com.example.dailroundsmarrowassessment1.data.local

import androidx.room.Entity

@Entity(tableName = "questions", primaryKeys = ["questionsUrl", "position"])
data class QuestionEntity(
    val questionsUrl: String,
    val position: Int,
    val questionId: Int,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
)
