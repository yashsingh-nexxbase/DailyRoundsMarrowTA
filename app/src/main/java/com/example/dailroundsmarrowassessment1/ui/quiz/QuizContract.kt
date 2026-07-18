package com.example.dailroundsmarrowassessment1.ui.quiz

import com.example.dailroundsmarrowassessment1.domain.Question

sealed interface QuizUiState {

    data object Loading : QuizUiState

    data class Error(val message: String) : QuizUiState

    data class Playing(
        val question: Question,
        val index: Int,
        val total: Int,
        val streak: Int,
        val reveal: Reveal? = null,
    ) : QuizUiState {
        val isLocked get() = reveal != null
    }

    data class Results(
        val correct: Int,
        val wrong: Int,
        val skipped: Int,
        val total: Int,
        val longestStreak: Int,
    ) : QuizUiState
}

data class Reveal(
    val selectedIndex: Int,
    val correctIndex: Int,
)

sealed interface QuizAction {
    data class SelectOption(val index: Int) : QuizAction
    data object Skip : QuizAction
    data object Restart : QuizAction
    data object Retry : QuizAction
}

sealed interface QuizEffect {
    data object StreakIgnited : QuizEffect
    data object StreakLost : QuizEffect
}
