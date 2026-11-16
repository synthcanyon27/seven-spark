package com.Rirsefu3.Degmed.core.navigation

import com.Rirsefu3.Degmed.domain.model.RoundResult

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object LevelMap : Screen

    data object HowToPlay : Screen

    data class Game(val levelId: Int, val attempt: Int) : Screen

    data class GameOver(val result: RoundResult) : Screen
}
