package com.example.dailroundsmarrowassessment1.ui.review

import com.example.dailroundsmarrowassessment1.domain.Question

sealed interface ReviewUiState {

    data object Loading : ReviewUiState

    data class Error(val message: String) : ReviewUiState

    data class Ready(
        val items: List<ReviewItem>,
        val correct: Int,
        val total: Int,
    ) : ReviewUiState
}

data class ReviewItem(
    val question: Question,
    val selectedIndex: Int,
)
