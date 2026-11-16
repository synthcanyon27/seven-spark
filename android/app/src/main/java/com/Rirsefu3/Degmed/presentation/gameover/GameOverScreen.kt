package com.Rirsefu3.Degmed.presentation.gameover

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
import com.Rirsefu3.Degmed.core.theme.BrassLight
import com.Rirsefu3.Degmed.core.theme.DangerDrift
import com.Rirsefu3.Degmed.core.theme.SurfaceRaised
import com.Rirsefu3.Degmed.core.theme.TextMuted
import com.Rirsefu3.Degmed.core.theme.TextSecondary
import com.Rirsefu3.Degmed.core.ui.GlassCard
import com.Rirsefu3.Degmed.core.ui.PrimaryButton
import com.Rirsefu3.Degmed.core.ui.ScreenBackground
import com.Rirsefu3.Degmed.core.ui.SecondaryButton
import com.Rirsefu3.Degmed.core.ui.StatCard
import com.Rirsefu3.Degmed.domain.model.RoundOutcome
import com.Rirsefu3.Degmed.domain.model.RoundResult

@Composable
fun GameOverScreen(
    result: RoundResult,
    onPlayAgain: (Int) -> Unit,
    onNextLevel: (Int) -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameOverViewModel = viewModel(
        factory = AppViewModelFactory(levelId = result.levelId, result = result),
        key = "gameover-${result.levelId}-${result.attempt}"
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    GameOverContent(
        state = state,
        onPlayAgain = { onPlayAgain(result.levelId) },
        onNextLevel = { result.nextLevelId?.let(onNextLevel) },
        onLevels = onLevels,
        onMenu = onMenu,
        modifier = modifier
    )
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onNextLevel: () -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val result = state.result
    val accent = if (state.won) AccentCyan else DangerDrift
    ScreenBackground(
        topColor = if (state.won) Color(0xCC120026) else Color(0xD90D1B2A),
        midColor = if (state.won) Color(0xB38B00FF) else Color(0xB3120026),
        bottomColor = Color(0xF20A0014),
        particleDensity = GameConfig.PARTICLES_RESULT,
        particlesEnabled = state.particlesEnabled,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(headlineRes(result.outcome)),
                style = MaterialTheme.typography.displaySmall,
                color = accent,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(result.titleRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            StarRow(earned = result.stars)

            Spacer(modifier = Modifier.height(18.dp))

            if (result.newBest) {
                Text(
                    text = stringResource(R.string.new_best_spark),
                    style = MaterialTheme.typography.labelSmall,
                    color = BrassLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceRaised.copy(alpha = 0.8f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            GlassCard(accent = AccentViolet, modifier = Modifier.fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(14.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatCard(
                            label = stringResource(R.string.stat_spark_earned),
                            value = result.spark.toString(),
                            accent = AccentViolet,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_goals),
                            value = stringResource(
                                R.string.fraction,
                                result.goalsDone,
                                result.goalsTotal
                            ),
                            accent = AccentBrass,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (result.movesLeft > 0 && result.bestChain > 1.0f) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            StatCard(
                                label = stringResource(R.string.stat_moves_left),
                                value = result.movesLeft.toString(),
                                accent = AccentCyan,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = stringResource(R.string.stat_best_chain),
                                value = stringResource(
                                    R.string.chain_value,
                                    result.bestChain.toString()
                                ),
                                accent = BrassLight,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (state.won && result.hasNextLevel) {
                PrimaryButton(
                    text = stringResource(R.string.next_sector),
                    onClick = onNextLevel
                )
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(
                    text = stringResource(R.string.play_again),
                    onClick = onPlayAgain,
                    accent = AccentViolet
                )
            } else {
                PrimaryButton(
                    text = stringResource(R.string.play_again),
                    onClick = onPlayAgain
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SecondaryButton(
                    text = stringResource(R.string.levels),
                    onClick = onLevels,
                    accent = AccentCyan,
                    height = 48.dp,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    accent = TextMuted,
                    height = 48.dp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StarRow(earned: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        for (index in 0 until 3) {
            val filled = index < earned
            val scale by animateFloatAsState(
                targetValue = if (filled) 1f else 0.82f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.spark_core_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(44.dp)
                        .scale(scale)
                        .alpha(if (filled) 1f else 0.18f)
                )
            }
        }
    }
}

private fun headlineRes(outcome: RoundOutcome): Int = when (outcome) {
    RoundOutcome.ALIGNED -> R.string.array_aligned
    RoundOutcome.OUT_OF_MOVES -> R.string.out_of_moves
    RoundOutcome.SIGNAL_LOST -> R.string.signal_lost
}
