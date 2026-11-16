package com.Rirsefu3.Degmed.presentation.game

import com.Rirsefu3.Degmed.core.config.GameConfig
import com.Rirsefu3.Degmed.domain.model.BoardState
import com.Rirsefu3.Degmed.domain.model.GameSettings
import com.Rirsefu3.Degmed.domain.model.ObjectiveProgress

enum class GamePhase { IDLE, SPINNING, EVALUATING, FINISHED }

data class GameUiState(
    val levelTitleRes: Int = 0,
    val expert: Boolean = false,
    val seed: Int = 0,
    val board: BoardState = BoardState(GameConfig.BOARD_COLS, GameConfig.BOARD_ROWS, emptyList()),
    val objectives: List<ObjectiveProgress> = emptyList(),
    val selectedColumn: Int = NO_COLUMN,
    val frozenRow: Int = BoardState.NO_FROZEN_ROW,
    val freezePickMode: Boolean = false,
    val movesLeft: Int = 0,
    val moveLimit: Int = 0,
    val spark: Int = 0,
    val drift: Int = 0,
    val driftLimit: Int = 3,
    val phase: GamePhase = GamePhase.IDLE,
    val paused: Boolean = false,
    val rewindAvailable: Boolean = false,
    val spinTick: Int = 0,
    val spinningColumn: Int = NO_COLUMN,
    val shakeTick: Int = 0,
    val settings: GameSettings = GameSettings()
) {
    val goalsDone: Int get() = objectives.count { it.completed }
    val goalsTotal: Int get() = objectives.size
    val canSpin: Boolean
        get() = phase == GamePhase.IDLE && !paused && selectedColumn != NO_COLUMN && movesLeft > 0

    companion object {
        const val NO_COLUMN = -1
    }
}
