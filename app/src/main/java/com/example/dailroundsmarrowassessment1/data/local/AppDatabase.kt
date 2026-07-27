package com.example.dailroundsmarrowassessment1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ModuleProgressEntity::class,
        ModuleEntity::class,
        QuestionEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moduleProgressDao(): ModuleProgressDao

    abstract fun moduleDao(): ModuleDao

    abstract fun questionDao(): QuestionDao
}
