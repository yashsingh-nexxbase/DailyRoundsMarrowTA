package com.example.dailroundsmarrowassessment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dailroundsmarrowassessment1.ui.PulseQuizApp
import com.example.dailroundsmarrowassessment1.ui.theme.QuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizTheme {
                PulseQuizApp()
            }
        }
    }
}
