package com.Rirsefu3.Degmed.domain.usecase

import com.Rirsefu3.Degmed.domain.model.Objective
import kotlin.math.ceil
import kotlin.math.roundToInt

class ScoreRoundUseCase {

    fun chainMultiplier(chainStep: Int): Float = when {
        chainStep <= 0 -> 1.0f
        chainStep == 1 -> 1.5f
        else -> 2.0f
    }

    fun objectiveSpark(objective: Objective, chainStep: Int): Int =
        (objective.reward * chainMultiplier(chainStep)).roundToInt()

    fun movesBonus(movesLeft: Int): Int = movesLeft * MOVES_BONUS_PER_MOVE

    fun stars(movesLeft: Int, moveLimit: Int, won: Boolean): Int {
        if (!won) return 0
        val three = ceil(moveLimit * THREE_STAR_RATIO).toInt()
        val two = ceil(moveLimit * TWO_STAR_RATIO).toInt()
        return when {
            movesLeft >= three -> 3
            movesLeft >= two -> 2
            else -> 1
        }
    }

    companion object {
        const val MOVES_BONUS_PER_MOVE = 5
        const val THREE_STAR_RATIO = 0.40f
        const val TWO_STAR_RATIO = 0.15f
    }
}
