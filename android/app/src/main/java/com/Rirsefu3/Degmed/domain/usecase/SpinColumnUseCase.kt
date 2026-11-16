package com.Rirsefu3.Degmed.domain.usecase

import com.Rirsefu3.Degmed.domain.model.BoardState
import com.Rirsefu3.Degmed.domain.model.TileModifier

class SpinColumnUseCase {

    operator fun invoke(board: BoardState, column: Int, frozenRow: Int): BoardState {
        val cascade = board.hasModifier(column, TileModifier.COG)
        val rotated = rotate(board, column, frozenRow)
        if (!cascade) return rotated
        return rotate(rotated, (column + 1) % board.cols, frozenRow)
    }

    private fun rotate(board: BoardState, column: Int, frozenRow: Int): BoardState {
        val tiles = board.tiles.toMutableList()
        val rows = board.movableRows(column, frozenRow)
        if (rows.size >= 2) {
            val last = tiles[rows.last() * board.cols + column]
            for (i in rows.size - 1 downTo 1) {
                val from = rows[i - 1] * board.cols + column
                val to = rows[i] * board.cols + column
                tiles[to] = tiles[from]
            }
            tiles[rows.first() * board.cols + column] = last
        }
        for (row in 0 until board.rows) {
            val index = row * board.cols + column
            val tile = tiles[index]
            if (tile.modifier == TileModifier.FLUX) {
                tiles[index] = tile.copy(value = tile.value % 9 + 1)
            }
        }
        return board.withTiles(tiles.toList())
    }
}
