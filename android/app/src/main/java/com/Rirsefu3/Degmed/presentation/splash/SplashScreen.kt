package com.Rirsefu3.Degmed.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Rirsefu3.Degmed.R
import com.Rirsefu3.Degmed.core.config.GameConfig
import com.Rirsefu3.Degmed.core.di.AppViewModelFactory
import com.Rirsefu3.Degmed.core.theme.AccentBrass
import com.Rirsefu3.Degmed.core.theme.AccentCyan
import com.Rirsefu3.Degmed.core.theme.AccentViolet
import com.Rirsefu3.Degmed.core.theme.SurfaceRaised
import com.Rirsefu3.Degmed.core.theme.TextMuted
import com.Rirsefu3.Degmed.core.theme.TextPrimary
import com.Rirsefu3.Degmed.core.theme.TextSecondary
import com.Rirsefu3.Degmed.core.ui.ParticleField

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory())
) {
    val finished by viewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashContent(modifier = modifier)
}

@Composable
private fun SplashContent(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val outerAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart)
    )
    val innerAngle by transition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart)
    )
    val corePulse by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Reverse)
    )

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xE60A0014),
                            Color(0xF2120026),
                            Color(0xFA0A0014)
                        )
                    )
                )
        )
        ParticleField(
            density = GameConfig.PARTICLES_SPLASH,
            enabled = true,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(196.dp)) {
                Canvas(modifier = Modifier
                    .size(196.dp)
                    .rotate(outerAngle)) {
                    drawCircle(
                        color = AccentViolet.copy(alpha = 0.40f),
                        radius = size.minDimension / 2f,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
                Canvas(modifier = Modifier
                    .size(160.dp)
                    .rotate(innerAngle)) {
                    drawCircle(
                        color = AccentCyan.copy(alpha = 0.28f),
                        radius = size.minDimension / 2f,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
                Canvas(modifier = Modifier.size(128.dp)) {
                    drawCircle(
                        color = AccentBrass.copy(alpha = 0.22f * corePulse + 0.08f),
                        radius = size.minDimension / 2f,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.logo_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(132.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = stringResource(R.string.brand_title),
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.brand_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(44.dp))

            LinearProgressIndicator(
                color = AccentCyan,
                trackColor = SurfaceRaised,
                modifier = Modifier
                    .width(180.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
