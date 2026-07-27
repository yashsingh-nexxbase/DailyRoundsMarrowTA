package com.example.dailroundsmarrowassessment1.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface QuizApi {

    @GET
    suspend fun fetchModules(@Url url: String): List<ModuleDto>

    @GET
    suspend fun fetchModuleQuestions(@Url url: String): List<QuestionDto>

    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com/"

        const val MODULES_URL =
            "https://gist.github.com/dr-samrat/ee986f16da9d8303c1acfd364ece22c5/raw"
    }
}
