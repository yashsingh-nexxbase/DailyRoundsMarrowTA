package com.example.dailroundsmarrowassessment1.domain

interface ModuleRepository {

    suspend fun getModules(): Result<List<Module>>

    suspend fun getQuestions(questionsUrl: String): Result<List<Question>>
}
