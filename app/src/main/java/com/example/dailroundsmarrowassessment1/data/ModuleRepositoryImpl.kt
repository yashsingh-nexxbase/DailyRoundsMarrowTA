package com.example.dailroundsmarrowassessment1.data

import com.example.dailroundsmarrowassessment1.data.remote.QuizApi
import com.example.dailroundsmarrowassessment1.domain.Module
import com.example.dailroundsmarrowassessment1.domain.ModuleRepository
import com.example.dailroundsmarrowassessment1.domain.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ModuleRepositoryImpl(
    private val api: QuizApi,
) : ModuleRepository {

    override suspend fun getModules(): Result<List<Module>> = withContext(Dispatchers.IO) {
        runCatching {
            val modules = api.fetchModules(QuizApi.MODULES_URL)
            require(modules.isNotEmpty()) { "Module list is empty" }
            modules.map { it.toDomain() }
        }
    }

    override suspend fun getQuestions(questionsUrl: String): Result<List<Question>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val questions = api.fetchModuleQuestions(questionsUrl)
                require(questions.isNotEmpty()) { "Question list is empty" }
                questions.map { it.toDomain() }
            }
        }
}
