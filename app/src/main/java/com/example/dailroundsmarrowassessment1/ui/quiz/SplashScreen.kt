package com.example.dailroundsmarrowassessment1.ui.quiz

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.dailroundsmarrowassessment1.ui.theme.SkyBlue
import com.example.dailroundsmarrowassessment1.ui.theme.Violet

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val pulse by rememberInfiniteTransition(label = "splash")
        .animateFloat(
            initialValue = 1f,
            targetValue = 1.06f,
            animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
            label = "logoPulse",
        )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .graphicsLayer { scaleX = pulse; scaleY = pulse }
                    .background(
                        Brush.linearGradient(listOf(Violet, SkyBlue)),
                        RoundedCornerShape(28.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                BoltMark(Modifier.size(44.dp))
            }
            Text(
                text = "PulseQuiz",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 20.dp),
            )
            Text(
                text = "10 questions · keep the streak alive",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            LinearProgressIndicator(
                modifier = Modifier.width(180.dp),
                color = Violet,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Text(
                text = "Fetching questions…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun BoltMark(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawPath(path = boltPath(size), color = Color.White)
    }
}

private fun boltPath(size: Size) = Path().apply {
    val w = size.width
    val h = size.height
    moveTo(0.56f * w, 0.04f * h)
    lineTo(0.20f * w, 0.56f * h)
    lineTo(0.40f * w, 0.56f * h)
    lineTo(0.31f * w, 0.96f * h)
    lineTo(0.77f * w, 0.40f * h)
    lineTo(0.54f * w, 0.40f * h)
    close()
}
