package com.example.dailroundsmarrowassessment1.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.dailroundsmarrowassessment1.QuizApplication
import com.example.dailroundsmarrowassessment1.domain.ModuleRepository
import com.example.dailroundsmarrowassessment1.domain.Question
import com.example.dailroundsmarrowassessment1.ui.QuizRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val moduleRepository: ModuleRepository,
    private val questionsUrl: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _effects = Channel<QuizEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private var questions: List<Question> = emptyList()
    private var index = 0
    private var correct = 0
    private var wrong = 0
    private var skipped = 0
    private var streak = 0
    private var longestStreak = 0

    private var advanceJob: Job? = null

    init {
        loadQuestions()
    }

    fun onAction(action: QuizAction) {
        when (action) {
            is QuizAction.SelectOption -> selectOption(action.index)
            QuizAction.Skip -> skip()
            QuizAction.Restart -> restart()
            QuizAction.Retry -> loadQuestions()
        }
    }

    private fun loadQuestions() {
        _uiState.value = QuizUiState.Loading
        viewModelScope.launch {
            val request = async { moduleRepository.getQuestions(questionsUrl) }
            delay(MIN_SPLASH_MILLIS) // let the splash breathe on fast networks
            request.await().fold(
                onSuccess = {
                    questions = it
                    startQuiz()
                },
                onFailure = {
                    _uiState.value = QuizUiState.Error(it.message ?: "Something went wrong")
                },
            )
        }
    }

    private fun startQuiz() {
        advanceJob?.cancel()
        index = 0
        correct = 0
        wrong = 0
        skipped = 0
        streak = 0
        longestStreak = 0
        _uiState.value = playingState()
    }

    private fun selectOption(selected: Int) {
        val current = _uiState.value as? QuizUiState.Playing ?: return
        if (current.isLocked) return

        val question = questions[index]
        if (selected == question.correctIndex) {
            correct++
            streak++
            longestStreak = maxOf(longestStreak, streak)
            if (streak == STREAK_MILESTONE) sendEffect(QuizEffect.StreakIgnited)
        } else {
            wrong++
            if (streak > 0) sendEffect(QuizEffect.StreakLost)
            streak = 0
        }

        _uiState.value = current.copy(
            streak = streak,
            reveal = Reveal(selectedIndex = selected, correctIndex = question.correctIndex),
        )

        advanceJob = viewModelScope.launch {
            delay(REVEAL_MILLIS)
            advance()
        }
    }

    private fun skip() {
        val current = _uiState.value as? QuizUiState.Playing ?: return
        if (current.isLocked) return
        skipped++
        advance()
    }

    private fun advance() {
        index++
        _uiState.value = if (index < questions.size) {
            playingState()
        } else {
            QuizUiState.Results(
                correct = correct,
                wrong = wrong,
                skipped = skipped,
                total = questions.size,
                longestStreak = longestStreak,
            )
        }
    }

    private fun restart() {
        if (_uiState.value !is QuizUiState.Results) return
        startQuiz()
    }

    private fun playingState() = QuizUiState.Playing(
        question = questions[index],
        index = index,
        total = questions.size,
        streak = streak,
    )

    private fun sendEffect(effect: QuizEffect) {
        _effects.trySend(effect)
    }

    companion object {
        const val REVEAL_MILLIS = 2000L
        const val STREAK_MILESTONE = 3
        const val MIN_SPLASH_MILLIS = 1600L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as QuizApplication
                val route = createSavedStateHandle().toRoute<QuizRoute>()
                QuizViewModel(
                    moduleRepository = app.container.moduleRepository,
                    questionsUrl = route.questionsUrl,
                )
            }
        }
    }
}
