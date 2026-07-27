package com.example.dailroundsmarrowassessment1.data

import com.example.dailroundsmarrowassessment1.data.local.ModuleProgressDao
import com.example.dailroundsmarrowassessment1.data.local.ModuleProgressEntity
import com.example.dailroundsmarrowassessment1.domain.ModuleProgress
import com.example.dailroundsmarrowassessment1.domain.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgressRepositoryImpl(
    private val dao: ModuleProgressDao,
) : ProgressRepository {

    override fun observeProgress(): Flow<Map<String, ModuleProgress>> =
        dao.observeAll().map { rows ->
            rows.associate { it.moduleId to it.toDomain() }
        }

    override suspend fun getProgress(moduleId: String): ModuleProgress? =
        dao.findById(moduleId)?.toDomain()

    override suspend fun saveProgress(progress: ModuleProgress) {
        dao.upsert(progress.toEntity())
    }
}

private fun ModuleProgressEntity.toDomain() = ModuleProgress(
    moduleId = moduleId,
    correct = correct,
    total = total,
    completed = completed,
    answers = answers,
)

private fun ModuleProgress.toEntity() = ModuleProgressEntity(
    moduleId = moduleId,
    correct = correct,
    total = total,
    completed = completed,
    answers = answers,
)
