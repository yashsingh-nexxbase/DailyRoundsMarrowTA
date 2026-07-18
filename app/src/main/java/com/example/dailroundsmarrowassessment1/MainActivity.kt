package com.example.dailroundsmarrowassessment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailroundsmarrowassessment1.ui.quiz.ErrorScreen
import com.example.dailroundsmarrowassessment1.ui.quiz.QuizAction
import com.example.dailroundsmarrowassessment1.ui.quiz.QuizScreen
import com.example.dailroundsmarrowassessment1.ui.quiz.QuizUiState
import com.example.dailroundsmarrowassessment1.ui.quiz.QuizViewModel
import com.example.dailroundsmarrowassessment1.ui.quiz.SplashScreen
import com.example.dailroundsmarrowassessment1.ui.theme.QuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizTheme {
                QuizRoot()
            }
        }
    }
}

@Composable
private fun QuizRoot(viewModel: QuizViewModel = viewModel(factory = QuizViewModel.Factory)) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
    ) {
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

            // placeholder until the results screen lands

            is QuizUiState.Results -> Text(
                text = "Score ${s.correct}/${s.total}",
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
