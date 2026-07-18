package com.example.dailroundsmarrowassessment1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// The quiz is a deliberately dark design, so a single scheme keeps
// colors consistent regardless of the system setting.
private val QuizColorScheme = darkColorScheme(
    primary = Violet,
    onPrimary = TextBright,
    secondary = SkyBlue,
    onSecondary = Night,
    background = Night,
    onBackground = TextBright,
    surface = Surface1,
    onSurface = TextBright,
    surfaceVariant = Surface2,
    onSurfaceVariant = TextDim,
    error = Wrong,
    onError = Night,
    outline = TextDim,
)

@Composable
fun QuizTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QuizColorScheme,
        typography = Typography,
        content = content,
    )
}
