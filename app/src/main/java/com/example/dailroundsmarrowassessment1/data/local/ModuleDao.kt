package com.example.dailroundsmarrowassessment1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ModuleDao {

    @Query("SELECT * FROM modules ORDER BY position")
    suspend fun getAll(): List<ModuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(modules: List<ModuleEntity>)
}
