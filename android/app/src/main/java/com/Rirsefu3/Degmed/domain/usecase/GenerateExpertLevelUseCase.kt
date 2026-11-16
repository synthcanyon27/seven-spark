package com.Rirsefu3.Degmed.domain.usecase

import com.Rirsefu3.Degmed.domain.model.BoardState
import com.Rirsefu3.Degmed.domain.model.LevelDefinition
import com.Rirsefu3.Degmed.domain.model.Objective
import com.Rirsefu3.Degmed.domain.model.SpectrumBand
import com.Rirsefu3.Degmed.domain.model.Tile
import com.Rirsefu3.Degmed.domain.model.TileModifier
import kotlin.random.Random

class GenerateExpertLevelUseCase(
    private val evaluate: EvaluateBoardUseCase,
    private val spin: SpinColumnUseCase
) {

    operator fun invoke(fallback: LevelDefinition, attempt: Int): LevelDefinition {
        for (pass in 0 until MAX_PASSES) {
            val seed = fallback.id.toLong() * 7919L xor ((attempt + pass).toLong() * 104729L)
            val candidate = build(fallback, Random(seed))
            if (candidate != null) return candidate
        }
        return fallback
    }

    private fun build(fallback: LevelDefinition, random: Random): LevelDefinition? {
        val cols = fallback.board.cols
        val rows = fallback.board.rows
        val bands = SpectrumBand.entries
        val tiles = MutableList(cols * rows) {
            Tile(random.nextInt(1, 10), bands[random.nextInt(bands.size)])
        }

        val scrambled = (0 until cols).shuffled(random).take(if (random.nextBoolean()) 2 else 3)
        val plan = scrambled.map { if (random.nextBoolean()) 1 else 2 }
        if (plan.sum() > MAX_SOLUTION_DEPTH) return null

        val free = (0 until cols).filterNot { scrambled.contains(it) }
        repeat(PRISM_COUNT) {
            val col = free[random.nextInt(free.size)]
            val row = random.nextInt(rows)
            val index = row * cols + col
            tiles[index] = tiles[index].copy(modifier = TileModifier.PRISM)
        }

        val runObjective = deriveRun(tiles, cols, rows, random)
        val tripleObjective = deriveTriple(tiles, cols, rows, random)
        val sumRow = random.nextInt(rows)
        val sumTarget = (0 until cols).sumOf { tiles[sumRow * cols + it].value }
        val objectives = listOf(runObjective, tripleObjective, Objective.SumLine(sumTarget))

        val solved = BoardState(cols, rows, tiles.toList())
        if (evaluate(solved, objectives, emptySet()).completedIndices.size != objectives.size) return null

        var start = solved
        scrambled.forEachIndexed { index, col ->
            val cycle = start.cycleLength(col)
            if (cycle >= 2) {
                repeat(cycle - plan[index]) {
                    start = spin(start, col, BoardState.NO_FROZEN_ROW)
                }
            }
        }
        if (evaluate(start, objectives, emptySet()).completedIndices.size >= objectives.size) return null

        return fallback.copy(objectives = objectives, board = start)
    }

    private fun deriveRun(
        tiles: MutableList<Tile>,
        cols: Int,
        rows: Int,
        random: Random
    ): Objective.SpectrumRun {
        var bestBand: SpectrumBand? = null
        var bestLength = 0
        for (row in 0 until rows) {
            for (band in SpectrumBand.entries) {
                var run = 0
                for (col in 0 until cols) {
                    val tile = tiles[row * cols + col]
                    val matches = tile.modifier == TileModifier.PRISM || tile.band == band
                    run = if (matches) run + 1 else 0
                    if (run >= MIN_RUN && run > bestLength) {
                        bestLength = run
                        bestBand = band
                    }
                }
            }
        }
        val found = bestBand
        if (found != null) return Objective.SpectrumRun(found, minOf(bestLength, MAX_RUN))
        val band = SpectrumBand.entries[random.nextInt(SpectrumBand.entries.size)]
        val row = random.nextInt(rows)
        val col = random.nextInt(cols - MIN_RUN + 1)
        for (k in 0 until MIN_RUN) {
            val index = row * cols + col + k
            tiles[index] = tiles[index].copy(band = band)
        }
        return Objective.SpectrumRun(band, MIN_RUN)
    }

    private fun deriveTriple(
        tiles: MutableList<Tile>,
        cols: Int,
        rows: Int,
        random: Random
    ): Objective.TripleMatch {
        for (row in 0 until rows) {
            for (col in 0..cols - 3) {
                val value = tiles[row * cols + col].value
                if ((0 until 3).all { tiles[row * cols + col + it].value == value }) {
                    return Objective.TripleMatch(value)
                }
            }
        }
        val value = SIGNATURE_VALUES[random.nextInt(SIGNATURE_VALUES.size)]
        val row = random.nextInt(rows)
        val col = random.nextInt(cols - 2)
        for (k in 0 until 3) {
            val index = row * cols + col + k
            tiles[index] = tiles[index].copy(value = value)
        }
        return Objective.TripleMatch(value)
    }

    companion object {
        const val MAX_PASSES = 20
        const val MAX_SOLUTION_DEPTH = 5
        const val PRISM_COUNT = 2
        const val MIN_RUN = 3
        const val MAX_RUN = 5
        private val SIGNATURE_VALUES = intArrayOf(7, 7, 7, 3, 5, 9)
    }
}
