package com.example.dailroundsmarrowassessment1.ui.modules

import com.example.dailroundsmarrowassessment1.domain.Module

sealed interface ModuleListUiState {

    data object Loading : ModuleListUiState

    data class Error(val message: String) : ModuleListUiState

    data class Ready(val items: List<ModuleListItem>) : ModuleListUiState
}

enum class ModuleStatus { NotStarted, Completed }

data class ModuleListItem(
    val module: Module,
    val status: ModuleStatus,
    val correct: Int?,
    val total: Int,
)
