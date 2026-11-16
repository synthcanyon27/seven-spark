package com.Rirsefu3.Degmed.domain.model

data class BoardState(
    val cols: Int,
    val rows: Int,
    val tiles: List<Tile>
) {
    fun at(col: Int, row: Int): Tile = tiles[row * cols + col]

    fun movableRows(col: Int, frozenRow: Int): List<Int> =
        (0 until rows).filter { it != frozenRow && at(col, it).modifier != TileModifier.ANCHOR }

    fun cycleLength(col: Int): Int = movableRows(col, NO_FROZEN_ROW).size

    fun hasModifier(col: Int, modifier: TileModifier): Boolean =
        (0 until rows).any { at(col, it).modifier == modifier }

    fun withTiles(next: List<Tile>): BoardState = copy(tiles = next)

    companion object {
        const val NO_FROZEN_ROW = -1
    }
}
