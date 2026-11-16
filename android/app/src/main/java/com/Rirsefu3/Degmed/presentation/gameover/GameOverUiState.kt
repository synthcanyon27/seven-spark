package com.Rirsefu3.Degmed.presentation.gameover

import com.Rirsefu3.Degmed.domain.model.RoundOutcome
import com.Rirsefu3.Degmed.domain.model.RoundResult

data class GameOverUiState(
    val result: RoundResult,
    val totalStars: Int,
    val bestSpark: Int,
    val particlesEnabled: Boolean = true
) {
    val won: Boolean get() = result.outcome == RoundOutcome.ALIGNED
}
