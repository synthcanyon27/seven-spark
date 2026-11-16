package com.Rirsefu3.Degmed.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.Rirsefu3.Degmed.domain.model.RoundResult
import com.Rirsefu3.Degmed.presentation.app.AppViewModel
import com.Rirsefu3.Degmed.presentation.game.GameViewModel
import com.Rirsefu3.Degmed.presentation.gameover.GameOverViewModel
import com.Rirsefu3.Degmed.presentation.levelmap.LevelMapViewModel
import com.Rirsefu3.Degmed.presentation.menu.MenuViewModel
import com.Rirsefu3.Degmed.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val levelId: Int = 1,
    private val attempt: Int = 0,
    private val result: RoundResult? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                ServiceLocator.progressRepository(),
                ServiceLocator.settingsRepository(),
                ServiceLocator.levelRepository()
            )
            modelClass.isAssignableFrom(LevelMapViewModel::class.java) -> LevelMapViewModel(
                ServiceLocator.progressRepository(),
                ServiceLocator.levelRepository(),
                ServiceLocator.settingsRepository()
            )
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                levelId = levelId,
                attempt = attempt,
                levels = ServiceLocator.levelRepository(),
                progress = ServiceLocator.progressRepository(),
                settings = ServiceLocator.settingsRepository(),
                spinColumn = ServiceLocator.spinColumn,
                evaluateBoard = ServiceLocator.evaluateBoard,
                scoreRound = ServiceLocator.scoreRound
            )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                result = requireNotNull(result),
                progress = ServiceLocator.progressRepository(),
                settings = ServiceLocator.settingsRepository()
            )
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
