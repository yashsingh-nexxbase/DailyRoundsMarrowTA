package com.example.dailroundsmarrowassessment1.ui.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dailroundsmarrowassessment1.QuizApplication
import com.example.dailroundsmarrowassessment1.domain.Module
import com.example.dailroundsmarrowassessment1.domain.ModuleRepository
import com.example.dailroundsmarrowassessment1.domain.ProgressRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ModuleListViewModel(
    private val moduleRepository: ModuleRepository,
    private val progressRepository: ProgressRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ModuleListUiState>(ModuleListUiState.Loading)
    val uiState: StateFlow<ModuleListUiState> = _uiState.asStateFlow()

    private var modules: List<Module> = emptyList()
    private var progressJob: Job? = null

    init {
        load()
    }

    fun load() {
        progressJob?.cancel()
        _uiState.value = ModuleListUiState.Loading
        viewModelScope.launch {
            moduleRepository.getModules().fold(
                onSuccess = {
                    modules = it
                    observeProgress()
                },
                onFailure = {
                    _uiState.value = ModuleListUiState.Error(it.message ?: "Something went wrong")
                },
            )
        }
    }

    private fun observeProgress() {
        progressJob = viewModelScope.launch {
            progressRepository.observeProgress().collect { progressById ->
                _uiState.value = ModuleListUiState.Ready(
                    modules.map { module ->
                        val progress = progressById[module.id]
                        ModuleListItem(
                            module = module,
                            status = if (progress?.completed == true) {
                                ModuleStatus.Completed
                            } else {
                                ModuleStatus.NotStarted
                            },
                            correct = progress?.correct,
                            total = progress?.total ?: DEFAULT_QUESTION_COUNT,
                        )
                    },
                )
            }
        }
    }

    companion object {
        const val DEFAULT_QUESTION_COUNT = 10

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as QuizApplication
                ModuleListViewModel(
                    app.container.moduleRepository,
                    app.container.progressRepository,
                )
            }
        }
    }
}
