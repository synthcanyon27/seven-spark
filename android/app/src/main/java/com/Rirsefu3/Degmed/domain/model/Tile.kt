package com.Rirsefu3.Degmed.domain.model

enum class SpectrumBand { VIOLET, CYAN, BRASS, JADE }

enum class TileModifier { NONE, PRISM, COG, ANCHOR, FLUX }

data class Tile(
    val value: Int,
    val band: SpectrumBand,
    val modifier: TileModifier = TileModifier.NONE
)
