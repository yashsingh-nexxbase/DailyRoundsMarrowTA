package com.example.dailroundsmarrowassessment1

import android.app.Application
import com.example.dailroundsmarrowassessment1.di.AppContainer

class QuizApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
