package com.example.dailroundsmarrowassessment1.ui.quiz.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dailroundsmarrowassessment1.ui.theme.Violet

@Composable
fun SegmentedProgress(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        repeat(total) { i ->
            val color by animateColorAsState(
                targetValue = when {
                    i < current -> Violet
                    i == current -> Violet.copy(alpha = 0.45f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                },
                label = "segment$i",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .background(color, RoundedCornerShape(3.dp)),
            )
        }
    }
}
