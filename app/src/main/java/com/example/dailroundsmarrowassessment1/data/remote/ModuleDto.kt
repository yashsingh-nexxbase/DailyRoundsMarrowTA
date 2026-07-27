package com.example.dailroundsmarrowassessment1.data.remote

import com.example.dailroundsmarrowassessment1.domain.Module
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModuleDto(
    val id: String,
    val title: String,
    val description: String,
    @SerialName("questions_url") val questionsUrl: String,
) {
    fun toDomain() = Module(
        id = id,
        title = title,
        description = description,
        questionsUrl = questionsUrl,
    )
}
