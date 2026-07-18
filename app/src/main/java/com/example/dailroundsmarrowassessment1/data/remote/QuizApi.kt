package com.example.dailroundsmarrowassessment1.data.remote

import retrofit2.http.GET

interface QuizApi {

    @GET("raw")
    suspend fun fetchQuestions(): List<QuestionDto>

    companion object {
        const val BASE_URL =
            "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/"
    }
}
