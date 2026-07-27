package com.example.dailroundsmarrowassessment1.ui.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dailroundsmarrowassessment1.ui.quiz.ErrorScreen
import com.example.dailroundsmarrowassessment1.ui.quiz.components.OptionRow
import com.example.dailroundsmarrowassessment1.ui.quiz.components.OptionVisual
import com.example.dailroundsmarrowassessment1.ui.theme.SkyBlue
import com.example.dailroundsmarrowassessment1.ui.theme.Violet
import com.example.dailroundsmarrowassessment1.ui.theme.Wrong

private const val OPTION_LETTERS = "ABCD"

@Composable
fun ReviewScreen(
    state: ReviewUiState,
    onDone: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ReviewUiState.Loading -> LoadingState(modifier)

        is ReviewUiState.Error -> ErrorScreen(
            message = state.message,
            onRetry = onRetry,
            modifier = modifier,
            title = "Couldn't load review",
        )

        is ReviewUiState.Ready -> ReviewContent(
            state = state,
            onDone = onDone,
            modifier = modifier,
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Violet)
    }
}

@Composable
private fun ReviewContent(
    state: ReviewUiState.Ready,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Header(correct = state.correct, total = state.total) }
        itemsIndexed(state.items) { i, item ->
            QuestionReviewCard(number = i + 1, item = item)
        }
        item { DoneButton(onDone) }
    }
}

@Composable
private fun Header(correct: Int, total: Int) {
    Column(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(
            text = "Review",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Score: $correct/$total",
            style = MaterialTheme.typography.bodyMedium,
            color = SkyBlue,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun QuestionReviewCard(number: Int, item: ReviewItem) {
    val skipped = item.selectedIndex == ReviewViewModel.SKIPPED

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "QUESTION $number",
                    style = MaterialTheme.typography.labelMedium,
                    color = SkyBlue,
                )
                if (skipped) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Skipped",
                        style = MaterialTheme.typography.labelMedium,
                        color = Wrong,
                    )
                }
            }
            Text(
                text = item.question.text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 10.dp, bottom = 14.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item.question.options.forEachIndexed { index, option ->
                    OptionRow(
                        letter = OPTION_LETTERS[index],
                        text = option,
                        visual = reviewVisual(index, item.selectedIndex, item.question.correctIndex),
                        enabled = false,
                        onClick = {},
                    )
                }
            }
        }
    }
}

@Composable
private fun DoneButton(onDone: () -> Unit) {
    Button(
        onClick = onDone,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(top = 8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Violet),
    ) {
        Text("Done", style = MaterialTheme.typography.labelLarge)
    }
}

private fun reviewVisual(index: Int, selectedIndex: Int, correctIndex: Int): OptionVisual = when {
    index == correctIndex -> OptionVisual.Correct
    index == selectedIndex -> OptionVisual.Wrong
    else -> OptionVisual.Faded
}
