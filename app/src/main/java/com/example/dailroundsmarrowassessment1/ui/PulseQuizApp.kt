package com.example.dailroundsmarrowassessment1.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dailroundsmarrowassessment1.ui.modules.ModuleListScreen
import com.example.dailroundsmarrowassessment1.ui.modules.ModuleListViewModel
import com.example.dailroundsmarrowassessment1.ui.quiz.QuizFlow
import com.example.dailroundsmarrowassessment1.ui.review.ReviewScreen
import com.example.dailroundsmarrowassessment1.ui.review.ReviewViewModel

@Composable
fun PulseQuizApp() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(modifier = Modifier.safeDrawingPadding()) {
            NavHost(
                navController = navController,
                startDestination = ModuleListRoute,
            ) {
                composable<ModuleListRoute> {
                    val viewModel: ModuleListViewModel =
                        viewModel(factory = ModuleListViewModel.Factory)
                    val state by viewModel.uiState.collectAsStateWithLifecycle()
                    ModuleListScreen(
                        state = state,
                        onStart = { module ->
                            navController.navigate(QuizRoute(module.id, module.questionsUrl))
                        },
                        onReview = { module ->
                            navController.navigate(ReviewRoute(module.id, module.questionsUrl))
                        },
                        onRetry = viewModel::load,
                    )
                }
                composable<QuizRoute> {
                    QuizFlow(onExit = { navController.popBackStack() })
                }
                composable<ReviewRoute> {
                    val viewModel: ReviewViewModel =
                        viewModel(factory = ReviewViewModel.Factory)
                    val state by viewModel.uiState.collectAsStateWithLifecycle()
                    ReviewScreen(
                        state = state,
                        onDone = { navController.popBackStack() },
                        onRetry = viewModel::load,
                    )
                }
            }
        }
    }
}
