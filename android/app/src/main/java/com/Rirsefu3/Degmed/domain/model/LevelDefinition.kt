package com.Rirsefu3.Degmed.domain.model

data class LevelDefinition(
    val id: Int,
    val titleRes: Int,
    val expert: Boolean,
    val moveLimit: Int,
    val driftLimit: Int,
    val allowRewind: Boolean,
    val starsRequired: Int,
    val objectives: List<Objective>,
    val board: BoardState
)

data class LevelProgress(
    val levelId: Int,
    val stars: Int,
    val bestSpark: Int
)

data class RoundResult(
    val levelId: Int,
    val titleRes: Int,
    val attempt: Int,
    val outcome: RoundOutcome,
    val spark: Int,
    val stars: Int,
    val movesLeft: Int,
    val goalsDone: Int,
    val goalsTotal: Int,
    val bestChain: Float,
    val expert: Boolean,
    val nextLevelId: Int?,
    val newBest: Boolean
) {
    val hasNextLevel: Boolean get() = nextLevelId != null
}

enum class RoundOutcome { ALIGNED, OUT_OF_MOVES, SIGNAL_LOST }

data class GameSettings(
    val numeralsOnTiles: Boolean = true,
    val bandLabels: Boolean = false,
    val particleTrails: Boolean = true
)
