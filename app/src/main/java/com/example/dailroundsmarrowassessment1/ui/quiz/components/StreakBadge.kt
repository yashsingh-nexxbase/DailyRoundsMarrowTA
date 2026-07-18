package com.example.dailroundsmarrowassessment1.ui.quiz.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.dailroundsmarrowassessment1.ui.quiz.QuizViewModel
import com.example.dailroundsmarrowassessment1.ui.theme.Ember
import com.example.dailroundsmarrowassessment1.ui.theme.TextDim

@Composable
fun StreakBadge(
    streak: Int,
    modifier: Modifier = Modifier,
) {
    val lit = streak >= QuizViewModel.STREAK_MILESTONE
    val warm = streak > 0

    val containerColor by animateColorAsState(
        targetValue = if (lit) Ember.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surface,
        label = "streakBg",
    )
    val borderColor by animateColorAsState(
        targetValue = if (lit) Ember.copy(alpha = 0.55f) else Color.White.copy(alpha = 0.08f),
        label = "streakBorder",
    )
    val flameColor by animateColorAsState(
        targetValue = if (warm) Ember else TextDim.copy(alpha = 0.5f),
        label = "flameColor",
    )

    // quick pop every time the count goes up
    val scale = remember { Animatable(1f) }
    LaunchedEffect(streak) {
        if (streak > 0) {
            scale.animateTo(1.18f, tween(120))
            scale.animateTo(1f, spring())
        }
    }

    // the flame dances once the badge is lit
    val wiggle by rememberInfiniteTransition(label = "flame")
        .animateFloat(
            initialValue = -6f,
            targetValue = 6f,
            animationSpec = infiniteRepeatable(tween(350), RepeatMode.Reverse),
            label = "flameWiggle",
        )

    Surface(
        modifier = modifier
            .graphicsLayer { scaleX = scale.value; scaleY = scale.value }
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(50))
            .semantics { contentDescription = "Streak $streak" + if (lit) ", on fire" else "" },
        shape = RoundedCornerShape(50),
        color = containerColor,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Canvas(
                modifier = Modifier
                    .size(16.dp)
                    .graphicsLayer { if (lit) rotationZ = wiggle },
            ) {
                drawPath(flamePath(size), flameColor)
            }
            Text(
                text = streak.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = if (warm) MaterialTheme.colorScheme.onSurface else TextDim,
            )
        }
    }
}

// flame outline, drawn in a 24x24 box and scaled to the canvas
private fun flamePath(size: Size): Path {
    val s = size.width / 24f
    return Path().apply {
        moveTo(12 * s, 2 * s)
        cubicTo(13 * s, 6 * s, 8 * s, 8 * s, 8 * s, 13 * s)
        cubicTo(8 * s, 16.3f * s, 10.7f * s, 19 * s, 14 * s, 19 * s)
        cubicTo(17.3f * s, 19 * s, 20 * s, 16.3f * s, 20 * s, 13 * s)
        cubicTo(20 * s, 10.5f * s, 18.8f * s, 8.6f * s, 17.5f * s, 7 * s)
        cubicTo(17.2f * s, 8.5f * s, 16.5f * s, 9.5f * s, 15.5f * s, 10 * s)
        cubicTo(16 * s, 6.5f * s, 14 * s, 3.5f * s, 12 * s, 2 * s)
        close()
    }
}
