package com.Rirsefu3.Degmed.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
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
import com.Rirsefu3.Degmed.core.theme.DangerDrift
import com.Rirsefu3.Degmed.core.theme.SurfaceDim
import com.Rirsefu3.Degmed.core.theme.SurfaceInstrument
import com.Rirsefu3.Degmed.core.theme.TextMuted
import com.Rirsefu3.Degmed.core.theme.TextPrimary
import com.Rirsefu3.Degmed.core.theme.TextSecondary
import com.Rirsefu3.Degmed.core.ui.ObjectiveChip
import com.Rirsefu3.Degmed.core.ui.PrimaryButton
import com.Rirsefu3.Degmed.core.ui.ScreenBackground
import com.Rirsefu3.Degmed.core.ui.SecondaryButton
import com.Rirsefu3.Degmed.core.ui.StatCard
import com.Rirsefu3.Degmed.core.ui.bandColor
import com.Rirsefu3.Degmed.domain.model.Objective
import com.Rirsefu3.Degmed.domain.model.SpectrumBand
import com.Rirsefu3.Degmed.domain.model.ObjectiveProgress
import com.Rirsefu3.Degmed.domain.model.RoundResult
import com.Rirsefu3.Degmed.presentation.dialog.PauseDialog
import kotlin.math.sin

@Composable
fun GameScreen(
    levelId: Int,
    attempt: Int,
    onGameOver: (RoundResult) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel(
        factory = AppViewModelFactory(levelId = levelId, attempt = attempt),
        key = "game-$levelId-$attempt"
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { result -> onGameOver(result) }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    GameContent(
        state = state,
        onCellTap = viewModel::onCellTap,
        onSpin = viewModel::onSpin,
        onFreeze = viewModel::onFreezeArmed,
        onRewind = viewModel::onRewind,
        onPause = viewModel::onPauseRequested,
        onMenu = onMenu,
        modifier = modifier
    )

    if (state.paused) {
        PauseDialog(
            levelTitle = stringResource(state.levelTitleRes),
            movesLeft = state.movesLeft,
            onResume = viewModel::onResume,
            onMenu = onMenu
        )
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onCellTap: (Int, Int) -> Unit,
    onSpin: () -> Unit,
    onFreeze: () -> Unit,
    onRewind: () -> Unit,
    onPause: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenBackground(
        topColor = Color(0xD90A0014),
        midColor = Color(0xB30D1B2A),
        bottomColor = Color(0xE6120026),
        particleDensity = GameConfig.PARTICLES_GAME,
        particlesEnabled = state.settings.particleTrails,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                titleRes = state.levelTitleRes,
                seed = state.seed,
                onMenu = onMenu,
                onPause = onPause
            )

            ObjectiveRow(
                objectives = state.objectives,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            ShakingBoard(
                state = state,
                onCellTap = onCellTap,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            Text(
                text = when {
                    state.freezePickMode -> stringResource(R.string.hint_freeze_pick)
                    state.selectedColumn != GameUiState.NO_COLUMN ->
                        stringResource(R.string.hint_spin_ready)
                    else -> stringResource(R.string.hint_select)
                },
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            ReadoutStrip(state = state)

            ControlsBlock(
                state = state,
                onSpin = onSpin,
                onFreeze = onFreeze,
                onRewind = onRewind
            )
        }
    }
}

@Composable
private fun GameHeader(
    titleRes: Int,
    seed: Int,
    onMenu: () -> Unit,
    onPause: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(SurfaceDim.copy(alpha = 0.55f))
            .padding(horizontal = 8.dp)
    ) {
        TextButton(onClick = onMenu, modifier = Modifier.height(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = stringResource(R.string.menu),
                style = MaterialTheme.typography.labelSmall,
                color = AccentCyan
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.seed_line, seed),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
        IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun ObjectiveRow(objectives: List<ObjectiveProgress>, modifier: Modifier = Modifier) {
    if (objectives.isEmpty()) return
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        objectives.forEach { progress ->
            ObjectiveChip(
                label = objectiveLabel(progress.objective),
                progress = if (progress.completed) {
                    stringResource(R.string.fraction_done, progress.current, progress.required)
                } else {
                    stringResource(R.string.fraction, progress.current, progress.required)
                },
                accent = objectiveAccent(progress.objective),
                iconRes = objectiveIcon(progress.objective),
                completed = progress.completed,
                iconDescription = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun objectiveLabel(objective: Objective): String = when (objective) {
    is Objective.SumLine -> stringResource(R.string.objective_sum, objective.target)
    is Objective.TripleMatch -> stringResource(R.string.objective_triple, objective.value)
    is Objective.SpectrumRun -> stringResource(
        R.string.objective_run,
        stringResource(bandNameResource(objective)),
        objective.length
    )
}

private fun bandNameResource(objective: Objective.SpectrumRun): Int = when (objective.band) {
    SpectrumBand.VIOLET -> R.string.band_violet_name
    SpectrumBand.CYAN -> R.string.band_cyan_name
    SpectrumBand.BRASS -> R.string.band_brass_name
    SpectrumBand.JADE -> R.string.band_jade_name
}

private fun objectiveAccent(objective: Objective): Color = when (objective) {
    is Objective.SumLine -> AccentCyan
    is Objective.TripleMatch -> AccentBrass
    is Objective.SpectrumRun -> bandColor(objective.band)
}

private fun objectiveIcon(objective: Objective): Int = when (objective) {
    is Objective.SumLine -> R.drawable.ic_sum
    is Objective.TripleMatch -> R.drawable.ic_triple
    is Objective.SpectrumRun -> R.drawable.ic_spectrum
}

@Composable
private fun ShakingBoard(
    state: GameUiState,
    onCellTap: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shake = remember { Animatable(0f) }
    LaunchedEffect(state.shakeTick) {
        if (state.shakeTick > 0) {
            shake.snapTo(1f)
            shake.animateTo(targetValue = 0f, animationSpec = tween(durationMillis = 220))
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.offset(x = 4.dp * sin(shake.value * 12f))
    ) {
        BoardView(state = state, onCellTap = onCellTap)
    }
}

@Composable
private fun ReadoutStrip(state: GameUiState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDim.copy(alpha = 0.65f))
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .semantics { liveRegion = LiveRegionMode.Polite }
    ) {
        StatCard(
            label = stringResource(R.string.stat_moves),
            value = state.movesLeft.toString(),
            accent = AccentCyan,
            announce = true,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = stringResource(R.string.stat_goals),
            value = stringResource(R.string.fraction, state.goalsDone, state.goalsTotal),
            accent = AccentBrass,
            announce = true,
            modifier = Modifier.weight(1f)
        )
        if (state.spark > 0) {
            StatCard(
                label = stringResource(R.string.stat_spark),
                value = state.spark.toString(),
                accent = AccentViolet,
                modifier = Modifier.weight(1f)
            )
        }
        if (state.drift > 0) {
            StatCard(
                label = stringResource(R.string.stat_drift),
                value = stringResource(R.string.fraction, state.drift, state.driftLimit),
                accent = DangerDrift,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ControlsBlock(
    state: GameUiState,
    onSpin: () -> Unit,
    onFreeze: () -> Unit,
    onRewind: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(SurfaceInstrument.copy(alpha = 0.92f))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SecondaryButton(
                text = stringResource(R.string.freeze),
                onClick = onFreeze,
                accent = AccentBrass,
                enabled = state.phase == GamePhase.IDLE && !state.paused,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.rewind),
                onClick = onRewind,
                accent = AccentCyan,
                enabled = state.rewindAvailable && state.spinTick > 0 &&
                    state.phase == GamePhase.IDLE && !state.paused,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(R.string.spin),
            onClick = onSpin,
            enabled = state.canSpin
        )
    }
}
