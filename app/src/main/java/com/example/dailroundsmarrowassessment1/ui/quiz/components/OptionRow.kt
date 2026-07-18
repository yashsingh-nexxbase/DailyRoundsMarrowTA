package com.example.dailroundsmarrowassessment1.ui.quiz.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.dailroundsmarrowassessment1.ui.theme.Correct
import com.example.dailroundsmarrowassessment1.ui.theme.Night
import com.example.dailroundsmarrowassessment1.ui.theme.TextDim
import com.example.dailroundsmarrowassessment1.ui.theme.Wrong

enum class OptionVisual { Idle, Correct, Wrong, Faded }

@Composable
fun OptionRow(
    letter: Char,
    text: String,
    visual: OptionVisual,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val container by animateColorAsState(
        targetValue = when (visual) {
            OptionVisual.Correct -> Correct.copy(alpha = 0.14f)
            OptionVisual.Wrong -> Wrong.copy(alpha = 0.14f)
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "optionBg",
    )
    val border by animateColorAsState(
        targetValue = when (visual) {
            OptionVisual.Correct -> Correct
            OptionVisual.Wrong -> Wrong
            else -> Color.Transparent
        },
        label = "optionBorder",
    )
    val alpha by animateFloatAsState(
        targetValue = if (visual == OptionVisual.Faded) 0.38f else 1f,
        label = "optionAlpha",
    )

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { this.alpha = alpha },
        shape = RoundedCornerShape(16.dp),
        color = container,
        border = BorderStroke(1.5.dp, border),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LetterChip(letter, visual)
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            when (visual) {
                OptionVisual.Correct -> Mark("✓", Correct)
                OptionVisual.Wrong -> Mark("✕", Wrong)
                else -> Unit
            }
        }
    }
}

@Composable
private fun LetterChip(letter: Char, visual: OptionVisual) {
    val background = when (visual) {
        OptionVisual.Correct -> Correct
        OptionVisual.Wrong -> Wrong
        else -> Color.White.copy(alpha = 0.06f)
    }
    val textColor = when (visual) {
        OptionVisual.Correct, OptionVisual.Wrong -> Night
        else -> TextDim
    }
    Surface(
        modifier = Modifier.size(28.dp),
        shape = RoundedCornerShape(9.dp),
        color = background,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = letter.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = textColor,
            )
        }
    }
}

@Composable
private fun Mark(symbol: String, color: Color) {
    Text(
        text = symbol,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
    )
}
