package com.example.dailroundsmarrowassessment1.ui.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.sp
import com.example.dailroundsmarrowassessment1.domain.Question
import com.example.dailroundsmarrowassessment1.ui.quiz.components.OptionRow
import com.example.dailroundsmarrowassessment1.ui.quiz.components.OptionVisual
import com.example.dailroundsmarrowassessment1.ui.quiz.components.SegmentedProgress
import com.example.dailroundsmarrowassessment1.ui.quiz.components.StreakBadge
import com.example.dailroundsmarrowassessment1.ui.theme.EmberDeep
import com.example.dailroundsmarrowassessment1.ui.theme.Ember
import com.example.dailroundsmarrowassessment1.ui.theme.Night
import com.example.dailroundsmarrowassessment1.ui.theme.SkyBlue
import com.example.dailroundsmarrowassessment1.ui.theme.Violet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

private const val OPTION_LETTERS = "ABCD"

@Composable
fun QuizScreen(
    state: QuizUiState.Playing,
    effects: Flow<QuizEffect>,
    onAction: (QuizAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    var showFireToast by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                QuizEffect.StreakIgnited -> {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    showFireToast = true
                    scope.launch {
                        delay(1800)
                        showFireToast = false
                    }
                }
                QuizEffect.StreakLost -> {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            SegmentedProgress(current = state.index, total = state.total)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QuestionCounter(state.index + 1, state.total)
                Spacer(Modifier.weight(1f))
                StreakBadge(streak = state.streak)
            }

            // Scrollable content region: the card is vertically centered when it
            // fits (portrait), and becomes scrollable when it can't (landscape /
            // short devices) so the question and every option stay reachable.
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .heightIn(min = maxHeight),
                    verticalArrangement = Arrangement.Center,
                ) {
                    AnimatedContent(
                        targetState = state,
                        contentKey = { it.index },
                        transitionSpec = {
                            (slideInHorizontally { it / 3 } + fadeIn(tween(250)))
                                .togetherWith(slideOutHorizontally { -it / 3 } + fadeOut(tween(200)))
                        },
                        label = "questionCard",
                    ) { playing ->
                        QuestionCard(
                            question = playing.question,
                            index = playing.index,
                            reveal = playing.reveal,
                            locked = playing.isLocked,
                            onAction = onAction,
                        )
                    }
                }
            }

            AdvanceCountdown(
                revealed = state.reveal != null,
                lastQuestion = state.index == state.total - 1,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "swipe right to skip →",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.weight(1f))
                OutlinedButton(
                    onClick = { onAction(QuizAction.Skip) },
                    enabled = !state.isLocked,
                ) {
                    Text("Skip", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        FireToast(
            visible = showFireToast,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 72.dp),
        )
    }
}

@Composable
private fun QuestionCounter(number: Int, total: Int) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontSize = 18.sp)) { append(number.toString()) }
            withStyle(
                SpanStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            ) { append("/$total") }
        },
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun QuestionCard(
    question: Question,
    index: Int,
    reveal: Reveal?,
    locked: Boolean,
    onAction: (QuizAction) -> Unit,
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val skipThreshold = with(LocalDensity.current) { 90.dp.toPx() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationX = offsetX.value * 0.55f
                rotationZ = offsetX.value * 0.006f
                alpha = 1f - (offsetX.value / 1500f).coerceIn(0f, 0.5f)
            }
            .pointerInput(locked) {
                if (locked) return@pointerInput
                detectHorizontalDragGestures(
                    onDragEnd = {
                        val shouldSkip = offsetX.value > skipThreshold
                        scope.launch { offsetX.animateTo(0f, tween(220)) }
                        if (shouldSkip) onAction(QuizAction.Skip)
                    },
                    onDragCancel = {
                        scope.launch { offsetX.animateTo(0f, tween(220)) }
                    },
                ) { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        // drag right only — left has no meaning here
                        offsetX.snapTo((offsetX.value + dragAmount).coerceAtLeast(0f))
                    }
                }
            },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            QuestionChip(index + 1)
            Text(
                text = question.text,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 14.dp, bottom = 20.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                question.options.forEachIndexed { i, option ->
                    OptionRow(
                        letter = OPTION_LETTERS[i],
                        text = option,
                        visual = optionVisual(i, reveal),
                        enabled = !locked,
                        onClick = { onAction(QuizAction.SelectOption(i)) },
                    )
                }
            }
        }
    }
}

private fun optionVisual(index: Int, reveal: Reveal?): OptionVisual = when {
    reveal == null -> OptionVisual.Idle
    index == reveal.correctIndex -> OptionVisual.Correct
    index == reveal.selectedIndex -> OptionVisual.Wrong
    else -> OptionVisual.Faded
}

@Composable
private fun QuestionChip(number: Int) {
    Surface(
        shape = RoundedCornerShape(50),
        color = SkyBlue.copy(alpha = 0.12f),
    ) {
        Text(
            text = "QUESTION $number",
            style = MaterialTheme.typography.labelMedium,
            color = SkyBlue,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp),
        )
    }
}

@Composable
private fun AdvanceCountdown(revealed: Boolean, lastQuestion: Boolean) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(revealed) {
        if (revealed) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = QuizViewModel.REVEAL_MILLIS.toInt(),
                    easing = LinearEasing,
                ),
            )
        } else {
            progress.snapTo(0f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .graphicsLayer { alpha = if (revealed) 1f else 0f },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LinearProgressIndicator(
            progress = { progress.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
            color = Violet,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Text(
            text = if (lastQuestion) "Results…" else "Next question…",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 7.dp),
        )
    }
}

@Composable
private fun FireToast(visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(Ember, EmberDeep)),
                    RoundedCornerShape(50),
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
        ) {
            Text(
                text = "🔥 You're on fire!",
                style = MaterialTheme.typography.labelLarge,
                color = Night,
            )
        }
    }
}
