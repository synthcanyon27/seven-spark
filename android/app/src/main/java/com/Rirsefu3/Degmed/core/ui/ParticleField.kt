package com.Rirsefu3.Degmed.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.Rirsefu3.Degmed.core.theme.AccentBrass
import com.Rirsefu3.Degmed.core.theme.AccentCyan
import com.Rirsefu3.Degmed.core.theme.AccentViolet
import kotlin.random.Random

private data class Mote(
    val x: Float,
    val y: Float,
    val radius: Float,
    val alpha: Float,
    val drift: Float,
    val color: Color
)

@Composable
fun ParticleField(
    density: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    if (!enabled || density <= 0) return
    val palette = remember { listOf(AccentCyan, AccentViolet, AccentBrass) }
    val motes = remember(density) {
        val random = Random(density * 7919)
        List(density) {
            Mote(
                x = random.nextFloat(),
                y = random.nextFloat(),
                radius = 1f + random.nextFloat() * 1.5f,
                alpha = 0.08f + random.nextFloat() * 0.27f,
                drift = 0.08f + random.nextFloat() * 0.22f,
                color = palette[random.nextInt(palette.size)]
            )
        }
    }
    val transition = rememberInfiniteTransition()
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        motes.forEach { mote ->
            val travelled = (mote.y - phase * mote.drift * 4f)
            val y = ((travelled % 1f) + 1f) % 1f
            drawCircle(
                color = mote.color.copy(alpha = mote.alpha),
                radius = mote.radius * 2.4f,
                center = Offset(mote.x * size.width, y * size.height)
            )
        }
    }
}
