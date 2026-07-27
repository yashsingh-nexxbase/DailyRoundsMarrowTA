package com.example.dailroundsmarrowassessment1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ModuleProgressDao {

    @Query("SELECT * FROM module_progress")
    fun observeAll(): Flow<List<ModuleProgressEntity>>

    @Query("SELECT * FROM module_progress WHERE moduleId = :moduleId")
    suspend fun findById(moduleId: String): ModuleProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ModuleProgressEntity)
}
