package com.example.dailroundsmarrowassessment1.ui.modules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.dailroundsmarrowassessment1.domain.Module
import com.example.dailroundsmarrowassessment1.ui.quiz.ErrorScreen
import com.example.dailroundsmarrowassessment1.ui.theme.SkyBlue
import com.example.dailroundsmarrowassessment1.ui.theme.Violet

@Composable
fun ModuleListScreen(
    state: ModuleListUiState,
    onStart: (Module) -> Unit,
    onReview: (Module) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ModuleListUiState.Loading -> LoadingState(modifier)

        is ModuleListUiState.Error -> ErrorScreen(
            message = state.message,
            onRetry = onRetry,
            modifier = modifier,
            title = "Couldn't load modules",
        )

        is ModuleListUiState.Ready -> ModuleList(
            items = state.items,
            onStart = onStart,
            onReview = onReview,
            modifier = modifier,
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Violet)
            Text(
                text = "Loading modules…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Composable
private fun ModuleList(
    items: List<ModuleListItem>,
    onStart: (Module) -> Unit,
    onReview: (Module) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Header() }
        items(items = items, key = { it.module.id }) { item ->
            ModuleCard(
                item = item,
                onClick = {
                    when (item.status) {
                        ModuleStatus.NotStarted -> onStart(item.module)
                        ModuleStatus.Completed -> onReview(item.module)
                    }
                },
            )
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "PulseQuiz",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Pick a module to begin",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun ModuleCard(
    item: ModuleListItem,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.module.title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = item.module.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 3.dp),
                )
                Text(
                    text = summaryFor(item),
                    style = MaterialTheme.typography.labelMedium,
                    color = SkyBlue,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
            Spacer(Modifier.width(14.dp))
            StatusButton(status = item.status, onClick = onClick)
        }
    }
}

@Composable
private fun StatusButton(status: ModuleStatus, onClick: () -> Unit) {
    when (status) {
        ModuleStatus.NotStarted -> Button(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Violet),
        ) {
            Text("Start", style = MaterialTheme.typography.labelLarge)
        }

        ModuleStatus.Completed -> OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Review", style = MaterialTheme.typography.labelLarge)
        }
    }
}

private fun summaryFor(item: ModuleListItem): String = buildString {
    append("${item.total} Questions")
    if (item.status == ModuleStatus.Completed && item.correct != null) {
        append("  ·  Score: ${item.correct}/${item.total}")
    }
}
