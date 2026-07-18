package com.example.dailroundsmarrowassessment1.domain

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
)
