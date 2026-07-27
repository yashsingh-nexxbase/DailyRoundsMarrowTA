package com.example.dailroundsmarrowassessment1.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dailroundsmarrowassessment1.ui.quiz.components.ConfettiBurst
import com.example.dailroundsmarrowassessment1.ui.quiz.components.ScoreRing
import com.example.dailroundsmarrowassessment1.ui.theme.Correct
import com.example.dailroundsmarrowassessment1.ui.theme.Ember
import com.example.dailroundsmarrowassessment1.ui.theme.SkyBlue
import com.example.dailroundsmarrowassessment1.ui.theme.Violet
import com.example.dailroundsmarrowassessment1.ui.theme.Wrong

@Composable
fun ResultsScreen(
    state: QuizUiState.Results,
    onAction: (QuizAction) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var statsVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { statsVisible = true }

    val goodScore = state.total > 0 && state.correct.toFloat() / state.total >= 0.7f

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "QUIZ COMPLETE",
                style = MaterialTheme.typography.labelMedium,
                color = SkyBlue,
            )
            Text(
                text = headlineFor(state.correct, state.total),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 22.dp),
            )

            ScoreRing(correct = state.correct, total = state.total)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp, bottom = 26.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                val stats = listOf(
                    StatItem("✓", Correct, "Correct", state.correct),
                    StatItem("✕", Wrong, "Wrong", state.wrong),
                    StatItem("↷", SkyBlue, "Skipped", state.skipped),
                    StatItem("🔥", Ember, "Longest streak", state.longestStreak),
                )
                stats.forEachIndexed { i, stat ->
                    AnimatedVisibility(
                        visible = statsVisible,
                        enter = fadeIn(tween(400, delayMillis = 350 + i * 130)) +
                                slideInVertically(tween(400, delayMillis = 350 + i * 130)) { it / 3 },
                    ) {
                        StatRow(stat, highlight = stat.label == "Longest streak")
                    }
                }
            }

            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Violet),
            ) {
                Text("Finish", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = { onAction(QuizAction.Restart) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text("Restart Quiz", style = MaterialTheme.typography.labelLarge)
            }
        }

        if (goodScore) {
            ConfettiBurst()
        }
    }
}

private fun headlineFor(correct: Int, total: Int): String {
    val ratio = if (total == 0) 0f else correct.toFloat() / total
    return when {
        ratio == 1f -> "Flawless victory! 🏆"
        ratio >= 0.7f -> "Sharp shooter!"
        ratio >= 0.4f -> "Solid effort!"
        else -> "Warm-up round done!"
    }
}

private data class StatItem(
    val icon: String,
    val tint: Color,
    val label: String,
    val value: Int,
)

@Composable
private fun StatRow(stat: StatItem, highlight: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (highlight) Ember.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
        border = if (highlight) BorderStroke(1.dp, Ember.copy(alpha = 0.35f)) else null,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(10.dp),
                color = stat.tint.copy(alpha = 0.14f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = stat.icon, color = stat.tint)
                }
            }
            Text(
                text = stat.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 12.dp),
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = stat.value.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = if (highlight) Ember else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
