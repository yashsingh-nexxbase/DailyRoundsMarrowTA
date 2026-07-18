package com.example.dailroundsmarrowassessment1.data

import com.example.dailroundsmarrowassessment1.data.remote.QuizApi
import com.example.dailroundsmarrowassessment1.domain.Question
import com.example.dailroundsmarrowassessment1.domain.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionRepositoryImpl(
    private val api: QuizApi,
) : QuestionRepository {

    override suspend fun getQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        runCatching {
            val questions = api.fetchQuestions()
            require(questions.isNotEmpty()) { "Question list is empty" }
            questions.map { it.toDomain() }
        }
    }
}
