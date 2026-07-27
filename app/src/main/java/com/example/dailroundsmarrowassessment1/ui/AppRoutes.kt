package com.example.dailroundsmarrowassessment1.ui

import kotlinx.serialization.Serializable

@Serializable
object ModuleListRoute

@Serializable
data class QuizRoute(val moduleId: String, val questionsUrl: String)

@Serializable
data class ReviewRoute(val moduleId: String, val questionsUrl: String)
