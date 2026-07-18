package com.example.dailroundsmarrowassessment1.data.remote

import com.example.dailroundsmarrowassessment1.domain.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
) {
    fun toDomain() = Question(
        id = id,
        text = question,
        options = options,
        correctIndex = correctOptionIndex,
    )
}
