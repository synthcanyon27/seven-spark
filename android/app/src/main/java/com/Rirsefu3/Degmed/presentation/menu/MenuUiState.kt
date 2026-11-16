package com.Rirsefu3.Degmed.presentation.menu

import com.Rirsefu3.Degmed.domain.model.GameSettings

data class MenuUiState(
    val totalStars: Int = 0,
    val maxStars: Int = 0,
    val bestSpark: Int = 0,
    val totalSpark: Int = 0,
    val currentLevelId: Int = 1,
    val settings: GameSettings = GameSettings(),
    val settingsVisible: Boolean = false,
    val resetConfirmVisible: Boolean = false
)
