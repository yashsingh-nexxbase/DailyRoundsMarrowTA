package com.example.dailroundsmarrowassessment1.domain

import kotlinx.coroutines.flow.Flow

interface ProgressRepository {

    fun observeProgress(): Flow<Map<String, ModuleProgress>>

    suspend fun getProgress(moduleId: String): ModuleProgress?

    suspend fun saveProgress(progress: ModuleProgress)
}
