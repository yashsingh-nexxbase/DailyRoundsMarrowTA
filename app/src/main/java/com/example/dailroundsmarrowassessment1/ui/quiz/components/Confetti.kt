package com.example.dailroundsmarrowassessment1.ui.quiz.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import com.example.dailroundsmarrowassessment1.ui.theme.Correct
import com.example.dailroundsmarrowassessment1.ui.theme.Ember
import com.example.dailroundsmarrowassessment1.ui.theme.SkyBlue
import com.example.dailroundsmarrowassessment1.ui.theme.Violet
import androidx.compose.runtime.withFrameNanos
import com.example.dailroundsmarrowassessment1.ui.theme.Wrong
import kotlin.random.Random

private class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val size: Float,
    val color: Color,
    var rotation: Float,
    val spin: Float,
    var life: Float,
)

@Composable
fun ConfettiBurst(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var particles by remember { mutableStateOf<List<Particle>>(emptyList()) }
    var frame by remember { mutableLongStateOf(0L) }

    LaunchedEffect(canvasSize) {
        if (canvasSize == IntSize.Zero) return@LaunchedEffect

        val colors = listOf(Violet, SkyBlue, Correct, Ember, Wrong, Color(0xFFFFD166))
        val unit = with(density) { 1.dp.toPx() }
        particles = List(110) {
            Particle(
                x = canvasSize.width / 2f + (Random.nextFloat() - 0.5f) * 80 * unit,
                y = canvasSize.height * 0.3f,
                vx = (Random.nextFloat() - 0.5f) * 7f * unit,
                vy = -(Random.nextFloat() * 6f + 2f) * unit,
                size = (Random.nextFloat() * 5f + 3f) * unit,
                color = colors.random(),
                rotation = Random.nextFloat() * 180f,
                spin = (Random.nextFloat() - 0.5f) * 14f,
                life = 1f,
            )
        }

        val gravity = 0.18f * unit
        while (particles.any { it.life > 0f }) {
            withFrameNanos { }
            particles.forEach { p ->
                p.x += p.vx
                p.y += p.vy
                p.vy += gravity
                p.rotation += p.spin
                p.life -= 0.008f
            }
            frame++
        }
        particles = emptyList()
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { canvasSize = it },
    ) {
        frame // read so each tick redraws
        particles.forEach { p ->
            if (p.life <= 0f || p.y > size.height) return@forEach
            rotate(degrees = p.rotation, pivot = Offset(p.x, p.y)) {
                drawRect(
                    color = p.color.copy(alpha = p.life.coerceIn(0f, 1f)),
                    topLeft = Offset(p.x - p.size / 2, p.y - p.size / 2),
                    size = Size(p.size, p.size * 0.6f),
                )
            }
        }
    }
}
