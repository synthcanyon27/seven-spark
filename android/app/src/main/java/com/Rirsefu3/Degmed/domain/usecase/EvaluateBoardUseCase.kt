package com.Rirsefu3.Degmed.domain.usecase

import com.Rirsefu3.Degmed.domain.model.BoardState
import com.Rirsefu3.Degmed.domain.model.Objective
import com.Rirsefu3.Degmed.domain.model.ObjectiveProgress
import com.Rirsefu3.Degmed.domain.model.SpectrumBand
import com.Rirsefu3.Degmed.domain.model.TileModifier
import kotlin.math.abs
import kotlin.math.max

class EvaluateBoardUseCase {

    data class Evaluation(
        val progress: List<ObjectiveProgress>,
        val completedIndices: Set<Int>,
        val proximity: Int
    )

    operator fun invoke(
        board: BoardState,
        objectives: List<Objective>,
        alreadyCompleted: Set<Int>
    ): Evaluation {
        val completed = alreadyCompleted.toMutableSet()
        var proximity = 0
        val progress = objectives.mapIndexed { index, objective ->
            val required = requirement(objective)
            val reached = reach(board, objective)
            if (reached >= required) completed += index
            val done = completed.contains(index)
            if (done) proximity += DONE_WEIGHT else proximity += proximityOf(board, objective)
            ObjectiveProgress(
                objective = objective,
                current = if (done) required else reached,
                required = required,
                completed = done
            )
        }
        return Evaluation(progress, completed.toSet(), proximity)
    }

    private fun requirement(objective: Objective): Int = when (objective) {
        is Objective.SumLine -> 1
        is Objective.TripleMatch -> 3
        is Objective.SpectrumRun -> objective.length
    }

    private fun reach(board: BoardState, objective: Objective): Int = when (objective) {
        is Objective.SumLine -> if (bestSumGap(board, objective.target) == 0) 1 else 0
        is Objective.TripleMatch -> minOf(longestValueRun(board, objective.value), 3)
        is Objective.SpectrumRun -> minOf(longestBandRun(board, objective.band), objective.length)
    }

    private fun proximityOf(board: BoardState, objective: Objective): Int = when (objective) {
        is Objective.SumLine -> max(0, SUM_SCALE - bestSumGap(board, objective.target))
        is Objective.TripleMatch -> longestValueRun(board, objective.value) * RUN_WEIGHT
        is Objective.SpectrumRun -> minOf(longestBandRun(board, objective.band), objective.length) * RUN_WEIGHT
    }

    private fun bestSumGap(board: BoardState, target: Int): Int {
        var best = Int.MAX_VALUE
        for (row in 0 until board.rows) {
            var sum = 0
            for (col in 0 until board.cols) sum += board.at(col, row).value
            val gap = abs(sum - target)
            if (gap < best) best = gap
        }
        return best
    }

    private fun longestValueRun(board: BoardState, value: Int): Int {
        var best = 0
        for (row in 0 until board.rows) {
            var run = 0
            for (col in 0 until board.cols) {
                run = if (board.at(col, row).value == value) run + 1 else 0
                if (run > best) best = run
            }
        }
        return best
    }

    private fun longestBandRun(board: BoardState, band: SpectrumBand): Int {
        var best = 0
        for (row in 0 until board.rows) {
            var run = 0
            for (col in 0 until board.cols) {
                val tile = board.at(col, row)
                val matches = tile.modifier == TileModifier.PRISM || tile.band == band
                run = if (matches) run + 1 else 0
                if (run > best) best = run
            }
        }
        return best
    }

    companion object {
        const val SUM_SCALE = 14
        const val RUN_WEIGHT = 4
        const val DONE_WEIGHT = 40
    }
}
