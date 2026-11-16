package com.Rirsefu3.Degmed.presentation.levelmap

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.Rirsefu3.Degmed.core.theme.SurfaceDim
import com.Rirsefu3.Degmed.core.theme.SurfaceRaised
import com.Rirsefu3.Degmed.core.theme.TextMuted
import com.Rirsefu3.Degmed.core.theme.TextPrimary
import com.Rirsefu3.Degmed.core.theme.TextSecondary
import com.Rirsefu3.Degmed.core.ui.ScreenBackground
import com.Rirsefu3.Degmed.core.ui.SecondaryButton
import com.Rirsefu3.Degmed.core.ui.SectionTitle

@Composable
fun LevelMapScreen(
    onOpenLevel: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LevelMapViewModel = viewModel(factory = AppViewModelFactory(), key = KEY_MAP)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LevelMapContent(
        state = state,
        onTab = viewModel::selectTab,
        onNode = { node ->
            if (node.unlocked) {
                onOpenLevel(node.level.id)
            } else {
                viewModel.onLockedNode()
            }
        },
        onMenu = onMenu,
        modifier = modifier
    )
}

@Composable
private fun LevelMapContent(
    state: LevelMapUiState,
    onTab: (MapTab) -> Unit,
    onNode: (MapNode) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenBackground(
        topColor = Color(0xE60A0014),
        midColor = Color(0xB30D1B2A),
        bottomColor = Color(0xE6120026),
        particleDensity = GameConfig.PARTICLES_MAP,
        particlesEnabled = state.particlesEnabled,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            MapHeader(
                totalStars = state.totalStars,
                maxStars = state.maxStars,
                onMenu = onMenu
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                TabButton(
                    label = stringResource(R.string.sectors),
                    selected = state.tab == MapTab.SECTORS,
                    onClick = { onTab(MapTab.SECTORS) },
                    modifier = Modifier.weight(1f)
                )
                TabButton(
                    label = stringResource(R.string.expert),
                    selected = state.tab == MapTab.EXPERT,
                    onClick = { onTab(MapTab.EXPERT) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SectionTitle(text = stringResource(R.string.map_title))

            Spacer(modifier = Modifier.height(10.dp))

            if (state.tab == MapTab.EXPERT && state.nodes.none { it.unlocked }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.map_expert_empty),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(state.nodes) { node ->
                        MapNodeTile(node = node, onClick = { onNode(node) })
                    }
                }
            }

            if (state.lockedMessageVisible) {
                Text(
                    text = stringResource(R.string.map_locked_message),
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentBrass,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                accent = AccentCyan,
                height = 48.dp,
                modifier = Modifier.padding(bottom = 14.dp)
            )
        }
    }
}

@Composable
private fun MapHeader(totalStars: Int, maxStars: Int, onMenu: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
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
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceRaised.copy(alpha = 0.72f))
                .padding(horizontal = 10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.spark_core_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.map_stars_badge, totalStars, maxStars),
                style = MaterialTheme.typography.labelSmall,
                color = BrassLight
            )
        }
    }
}

@Composable
private fun TabButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            if (selected) AccentCyan else TextMuted.copy(alpha = 0.5f)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) {
                AccentViolet.copy(alpha = 0.45f)
            } else {
                SurfaceDim.copy(alpha = 0.5f)
            },
            contentColor = if (selected) TextPrimary else TextSecondary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .semantics { stateDescription = label }
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun MapNodeTile(node: MapNode, onClick: () -> Unit) {
    val accent = when {
        !node.unlocked -> TextMuted
        node.stars >= 3 -> AccentBrass
        else -> AccentCyan
    }
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(6.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = if (node.unlocked) 0.6f else 0.3f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (node.unlocked) {
                SurfaceDim.copy(alpha = 0.82f)
            } else {
                SurfaceRaised.copy(alpha = 0.55f)
            },
            contentColor = accent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .semantics { stateDescription = node.ordinal.toString() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (node.unlocked) {
                Text(
                    text = node.ordinal.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = accent
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (index in 0 until 3) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index < node.stars) {
                                        AccentBrass
                                    } else {
                                        TextMuted.copy(alpha = 0.4f)
                                    }
                                )
                        )
                    }
                }
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (node.level.expert) {
                        stringResource(R.string.map_needs_stars, node.level.starsRequired)
                    } else {
                        stringResource(R.string.map_locked)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private const val KEY_MAP = "levelmap"
