package com.example.dailroundsmarrowassessment1.di

import android.content.Context
import androidx.room.Room
import com.example.dailroundsmarrowassessment1.data.ModuleRepositoryImpl
import com.example.dailroundsmarrowassessment1.data.ProgressRepositoryImpl
import com.example.dailroundsmarrowassessment1.data.local.AppDatabase
import com.example.dailroundsmarrowassessment1.data.remote.QuizApi
import com.example.dailroundsmarrowassessment1.domain.ModuleRepository
import com.example.dailroundsmarrowassessment1.domain.ProgressRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppContainer(context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private val api: QuizApi = Retrofit.Builder()
        .baseUrl(QuizApi.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(QuizApi::class.java)

    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "pulsequiz.db",
    ).build()

    val moduleRepository: ModuleRepository = ModuleRepositoryImpl(api)

    val progressRepository: ProgressRepository =
        ProgressRepositoryImpl(database.moduleProgressDao())
}
