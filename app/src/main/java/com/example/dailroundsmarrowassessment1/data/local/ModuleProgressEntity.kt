package com.example.dailroundsmarrowassessment1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module_progress")
data class ModuleProgressEntity(
    @PrimaryKey val moduleId: String,
    val correct: Int,
    val total: Int,
    val completed: Boolean,
    val answers: List<Int>,
)
