package com.Rirsefu3.Degmed.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Rirsefu3.Degmed.core.di.AppViewModelFactory
import com.Rirsefu3.Degmed.core.navigation.Screen
import com.Rirsefu3.Degmed.core.theme.BackgroundBase
import com.Rirsefu3.Degmed.presentation.gameover.GameOverScreen
import com.Rirsefu3.Degmed.presentation.game.GameScreen
import com.Rirsefu3.Degmed.presentation.howto.HowToPlayScreen
import com.Rirsefu3.Degmed.presentation.levelmap.LevelMapScreen
import com.Rirsefu3.Degmed.presentation.menu.MenuScreen
import com.Rirsefu3.Degmed.presentation.splash.SplashScreen

@Composable
fun AppRoot(
    viewModel: AppViewModel = viewModel(factory = AppViewModelFactory(), key = KEY_APP)
) {
    val screen by viewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen !is Screen.Menu && screen !is Screen.Splash) {
        viewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBase)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(tween(durationMillis = 320)) togetherWith fadeOut(tween(durationMillis = 260))
            }
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashScreen(onFinished = viewModel::onSplashFinished)
                is Screen.Menu -> MenuScreen(
                    onPlay = viewModel::openLevel,
                    onLevels = viewModel::openLevelMap,
                    onHowToPlay = viewModel::openHowToPlay
                )
                is Screen.LevelMap -> LevelMapScreen(
                    onOpenLevel = viewModel::openLevel,
                    onMenu = viewModel::goToMenu
                )
                is Screen.HowToPlay -> HowToPlayScreen(onMenu = viewModel::goToMenu)
                is Screen.Game -> GameScreen(
                    levelId = current.levelId,
                    attempt = current.attempt,
                    onGameOver = viewModel::onRoundFinished,
                    onMenu = viewModel::goToMenu
                )
                is Screen.GameOver -> GameOverScreen(
                    result = current.result,
                    onPlayAgain = viewModel::replayLevel,
                    onNextLevel = viewModel::replayLevel,
                    onLevels = viewModel::openLevelMap,
                    onMenu = viewModel::goToMenu
                )
            }
        }
    }
}

private const val KEY_APP = "app"
