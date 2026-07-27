package com.example.dailroundsmarrowassessment1.domain

data class ModuleProgress(
    val moduleId: String,
    val correct: Int,
    val total: Int,
    val completed: Boolean,
    val answers: List<Int>,
)
