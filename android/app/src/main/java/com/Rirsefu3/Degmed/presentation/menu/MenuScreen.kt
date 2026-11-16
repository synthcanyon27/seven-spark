package com.Rirsefu3.Degmed.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
import com.Rirsefu3.Degmed.core.theme.SurfaceRaised
import com.Rirsefu3.Degmed.core.theme.TextMuted
import com.Rirsefu3.Degmed.core.theme.TextPrimary
import com.Rirsefu3.Degmed.core.theme.TextSecondary
import com.Rirsefu3.Degmed.core.ui.GlassCard
import com.Rirsefu3.Degmed.core.ui.PrimaryButton
import com.Rirsefu3.Degmed.core.ui.ScreenBackground
import com.Rirsefu3.Degmed.core.ui.StatCard
import com.Rirsefu3.Degmed.presentation.dialog.ResetConfirmDialog
import com.Rirsefu3.Degmed.presentation.dialog.SettingsDialog

@Composable
fun MenuScreen(
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onHowToPlay: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory(), key = KEY_MENU)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MenuContent(
        state = state,
        onPlay = { onPlay(state.currentLevelId) },
        onLevels = onLevels,
        onHowToPlay = onHowToPlay,
        onSettings = viewModel::openSettings,
        modifier = modifier
    )
    if (state.settingsVisible) {
        SettingsDialog(
            settings = state.settings,
            onNumerals = viewModel::setNumerals,
            onBandLabels = viewModel::setBandLabels,
            onParticles = viewModel::setParticles,
            onRequestReset = viewModel::requestReset,
            onClose = viewModel::closeSettings
        )
    }
    if (state.resetConfirmVisible) {
        ResetConfirmDialog(
            onConfirm = viewModel::confirmReset,
            onCancel = viewModel::dismissReset
        )
    }
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onHowToPlay: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenBackground(
        topColor = Color(0xB30A0014),
        midColor = Color(0xCC120026),
        bottomColor = Color(0xE60D1B2A),
        particleDensity = GameConfig.PARTICLES_MENU,
        particlesEnabled = state.settings.particleTrails,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            MenuHeader(totalSpark = state.totalSpark)

            Spacer(modifier = Modifier.height(16.dp))

            val hasStats = state.totalStars > 0 && state.bestSpark > 0
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PlayTile(
                    onPlay = onPlay,
                    modifier = Modifier.weight(if (hasStats) 1.6f else 1f)
                )
                if (hasStats) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        StatCard(
                            label = stringResource(R.string.menu_stars_label),
                            value = stringResource(
                                R.string.map_stars_badge,
                                state.totalStars,
                                state.maxStars
                            ),
                            accent = AccentBrass
                        )
                        StatCard(
                            label = stringResource(R.string.menu_best_label),
                            value = state.bestSpark.toString(),
                            accent = AccentCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            BentoTile(
                label = stringResource(R.string.levels),
                caption = stringResource(R.string.menu_levels_caption),
                accent = AccentCyan,
                onClick = onLevels,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BentoTile(
                    label = stringResource(R.string.how_to_play),
                    caption = stringResource(R.string.menu_howto_caption),
                    accent = AccentBrass,
                    onClick = onHowToPlay,
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                )
                BentoTile(
                    label = stringResource(R.string.settings),
                    caption = stringResource(R.string.menu_settings_caption),
                    accent = AccentViolet,
                    onClick = onSettings,
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.fair_play_footer),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun MenuHeader(totalSpark: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = stringResource(R.string.brand_title),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (totalSpark > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceRaised.copy(alpha = 0.72f))
                    .border(1.dp, AccentBrass.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.spark_core_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = totalSpark.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = BrassLight
                )
            }
        }
    }
}

@Composable
private fun PlayTile(onPlay: () -> Unit, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(tween(2400), RepeatMode.Reverse)
    )
    GlassCard(
        accent = AccentCyan,
        modifier = modifier
            .height(188.dp)
            .scale(breathe),
        fill = AccentViolet.copy(alpha = 0.22f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(AccentViolet.copy(alpha = 0.35f), Color(0x665E00B0))
                    )
                )
                .padding(14.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(52.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.menu_play_caption),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay,
                height = 56.dp
            )
        }
    }
}

@Composable
private fun BentoTile(
    label: String,
    caption: String,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.45f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceRaised.copy(alpha = 0.6f),
            contentColor = TextPrimary
        ),
        modifier = modifier.heightIn(min = 56.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = accent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

private const val KEY_MENU = "menu"
