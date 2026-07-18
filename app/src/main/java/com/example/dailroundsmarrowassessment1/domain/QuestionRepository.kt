package com.example.dailroundsmarrowassessment1.domain

interface QuestionRepository {
    suspend fun getQuestions(): Result<List<Question>>
}
