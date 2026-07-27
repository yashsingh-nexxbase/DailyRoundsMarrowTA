package com.example.dailroundsmarrowassessment1.data.remote

import com.example.dailroundsmarrowassessment1.domain.Question
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class QuestionDto(
    val id: Int,
    val question: String,
    val options: List<String>,
    @JsonNames("correctOption") val correctOptionIndex: Int,
) {
    fun toDomain() = Question(
        id = id,
        text = question,
        options = options,
        correctIndex = correctOptionIndex,
    )
}
