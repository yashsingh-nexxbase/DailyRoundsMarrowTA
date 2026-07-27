package com.example.dailroundsmarrowassessment1.ui.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuizFlow(
    onExit: () -> Unit,
    viewModel: QuizViewModel = viewModel(factory = QuizViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (val s = state) {
        QuizUiState.Loading -> SplashScreen()

        is QuizUiState.Error -> ErrorScreen(
            message = s.message,
            onRetry = { viewModel.onAction(QuizAction.Retry) },
        )

        is QuizUiState.Playing -> QuizScreen(
            state = s,
            effects = viewModel.effects,
            onAction = viewModel::onAction,
        )

        is QuizUiState.Results -> ResultsScreen(
            state = s,
            onAction = viewModel::onAction,
            onFinish = onExit,
        )
    }
}
