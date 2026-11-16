package com.Rirsefu3.Degmed.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Rirsefu3.Degmed.R
import com.Rirsefu3.Degmed.core.config.GameConfig
import com.Rirsefu3.Degmed.core.theme.AccentBrass
import com.Rirsefu3.Degmed.core.theme.AccentCyan
import com.Rirsefu3.Degmed.core.theme.AccentViolet
import com.Rirsefu3.Degmed.core.theme.SurfaceInstrument
import com.Rirsefu3.Degmed.core.ui.HudBrackets
import com.Rirsefu3.Degmed.core.ui.TileCell
import com.Rirsefu3.Degmed.domain.model.BoardState
import com.Rirsefu3.Degmed.domain.model.SpectrumBand
import com.Rirsefu3.Degmed.domain.model.TileModifier

private val BoardPad = 6.dp
private val BoardBorder = 2.dp
private val BoardFrame = BoardPad + BoardBorder
private val BoardMaxWidth = 360.dp
private val HeightGuard = 2000.dp

@Composable
fun BoardView(
    state: GameUiState,
    onCellTap: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val board = state.board
    if (board.tiles.isEmpty()) return
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val widthBudget = if (maxWidth < BoardMaxWidth) maxWidth else BoardMaxWidth
        val cellByWidth = ((widthBudget - BoardFrame * 2).value / board.cols).toInt().dp
        val heightBudget = if (maxHeight > HeightGuard) HeightGuard else maxHeight
        val cellByHeight = ((heightBudget - BoardFrame * 2).value / board.rows).toInt().dp
        val cell = if (cellByWidth < cellByHeight) cellByWidth else cellByHeight
        val boardWidth = cell * board.cols + BoardFrame * 2
        val boardHeight = cell * board.rows + BoardFrame * 2

        val slide = remember { Animatable(0f) }
        LaunchedEffect(state.spinTick) {
            if (state.spinTick > 0) {
                slide.snapTo(1f)
                slide.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = GameConfig.SPIN_ANIM_MS.toInt(),
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }

        val shape = RoundedCornerShape(16.dp)
        val movingRows = remember(state.spinTick, state.spinningColumn, state.frozenRow) {
            if (state.spinningColumn == GameUiState.NO_COLUMN) {
                emptyList()
            } else {
                board.movableRows(state.spinningColumn, state.frozenRow)
            }
        }

        Box(
            modifier = Modifier
                .width(boardWidth)
                .height(boardHeight)
                .clip(shape)
                .background(SurfaceInstrument.copy(alpha = 0.78f))
                .border(BoardBorder, AccentViolet.copy(alpha = 0.35f), shape)
                .padding(BoardPad)
        ) {
            Column {
                for (row in 0 until board.rows) {
                    Row {
                        for (col in 0 until board.cols) {
                            val tile = board.at(col, row)
                            val moving = col == state.spinningColumn && movingRows.contains(row)
                            val isTop = movingRows.firstOrNull() == row
                            TileCell(
                                tile = tile,
                                cellSize = cell,
                                frozen = row == state.frozenRow,
                                showNumerals = state.settings.numeralsOnTiles,
                                showBandLabel = state.settings.bandLabels,
                                description = stringResource(
                                    R.string.tile_description,
                                    stringResource(bandNameRes(tile.band)),
                                    tile.value
                                ),
                                stateLabel = if (row == state.frozenRow) {
                                    stringResource(R.string.tile_state_frozen)
                                } else {
                                    stringResource(R.string.tile_state_active)
                                },
                                spriteDescription = spriteDescription(tile.modifier),
                                modifier = Modifier
                                    .offset(y = if (moving) -(cell * slide.value) else 0.dp)
                                    .alpha(if (moving && isTop) 1f - slide.value else 1f)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { onCellTap(col, row) }
                            )
                        }
                    }
                }
            }

            if (state.frozenRow != BoardState.NO_FROZEN_ROW) {
                Box(
                    modifier = Modifier
                        .offset(y = cell * state.frozenRow)
                        .fillMaxWidth()
                        .height(cell)
                        .border(2.dp, AccentBrass.copy(alpha = 0.75f), RoundedCornerShape(6.dp))
                )
            }

            if (state.selectedColumn != GameUiState.NO_COLUMN) {
                Box(
                    modifier = Modifier
                        .offset(x = cell * state.selectedColumn)
                        .width(cell)
                        .height(cell * board.rows)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = cell, height = cell * board.rows)
                            .border(2.dp, AccentCyan.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                    )
                    HudBrackets(
                        color = AccentCyan,
                        modifier = Modifier.size(width = cell, height = cell * board.rows),
                        armLength = 10.dp
                    )
                    Text(
                        text = stringResource(R.string.column_index, state.selectedColumn + 1),
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentCyan,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}

private fun bandNameRes(band: SpectrumBand): Int = when (band) {
    SpectrumBand.VIOLET -> R.string.band_violet_name
    SpectrumBand.CYAN -> R.string.band_cyan_name
    SpectrumBand.BRASS -> R.string.band_brass_name
    SpectrumBand.JADE -> R.string.band_jade_name
}

@Composable
private fun spriteDescription(modifier: TileModifier): String? = when (modifier) {
    TileModifier.PRISM -> stringResource(R.string.prism_tile)
    TileModifier.COG -> stringResource(R.string.cog_tile)
    TileModifier.ANCHOR -> stringResource(R.string.anchor_tile)
    TileModifier.FLUX -> stringResource(R.string.flux_tile)
    TileModifier.NONE -> null
}
