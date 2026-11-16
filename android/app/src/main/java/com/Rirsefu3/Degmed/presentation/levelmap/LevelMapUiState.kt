package com.Rirsefu3.Degmed.presentation.levelmap

import com.Rirsefu3.Degmed.domain.model.LevelDefinition

enum class MapTab { SECTORS, EXPERT }

data class MapNode(
    val level: LevelDefinition,
    val unlocked: Boolean,
    val stars: Int,
    val ordinal: Int
)

data class LevelMapUiState(
    val tab: MapTab = MapTab.SECTORS,
    val nodes: List<MapNode> = emptyList(),
    val totalStars: Int = 0,
    val maxStars: Int = 0,
    val lockedMessageVisible: Boolean = false,
    val particlesEnabled: Boolean = true
)
