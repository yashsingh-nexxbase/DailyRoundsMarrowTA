package com.example.dailroundsmarrowassessment1.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.dailroundsmarrowassessment1.QuizApplication
import com.example.dailroundsmarrowassessment1.domain.ModuleRepository
import com.example.dailroundsmarrowassessment1.domain.ProgressRepository
import com.example.dailroundsmarrowassessment1.ui.ReviewRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val moduleRepository: ModuleRepository,
    private val progressRepository: ProgressRepository,
    private val moduleId: String,
    private val questionsUrl: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReviewUiState>(ReviewUiState.Loading)
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = ReviewUiState.Loading
        viewModelScope.launch {
            moduleRepository.getQuestions(questionsUrl).fold(
                onSuccess = { questions ->
                    val progress = progressRepository.getProgress(moduleId)
                    val answers = progress?.answers ?: emptyList()
                    _uiState.value = ReviewUiState.Ready(
                        items = questions.mapIndexed { i, question ->
                            ReviewItem(
                                question = question,
                                selectedIndex = answers.getOrElse(i) { SKIPPED },
                            )
                        },
                        correct = progress?.correct ?: 0,
                        total = progress?.total ?: questions.size,
                    )
                },
                onFailure = {
                    _uiState.value = ReviewUiState.Error(it.message ?: "Something went wrong")
                },
            )
        }
    }

    companion object {
        const val SKIPPED = -1

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as QuizApplication
                val route = createSavedStateHandle().toRoute<ReviewRoute>()
                ReviewViewModel(
                    moduleRepository = app.container.moduleRepository,
                    progressRepository = app.container.progressRepository,
                    moduleId = route.moduleId,
                    questionsUrl = route.questionsUrl,
                )
            }
        }
    }
}
