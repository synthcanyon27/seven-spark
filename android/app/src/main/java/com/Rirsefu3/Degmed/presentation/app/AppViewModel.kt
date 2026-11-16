package com.Rirsefu3.Degmed.presentation.app

import androidx.lifecycle.ViewModel
import com.Rirsefu3.Degmed.core.navigation.Screen
import com.Rirsefu3.Degmed.domain.model.RoundResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val backStack = mutableListOf<Screen>()
    private var attempts = 0

    fun onSplashFinished() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun openLevel(levelId: Int) {
        attempts += 1
        push(Screen.Game(levelId, attempts))
    }

    fun replayLevel(levelId: Int) {
        attempts += 1
        backStack.removeAll { it is Screen.Game || it is Screen.GameOver }
        _screen.value = Screen.Game(levelId, attempts)
    }

    fun openLevelMap() {
        push(Screen.LevelMap)
    }

    fun openHowToPlay() {
        push(Screen.HowToPlay)
    }

    fun onRoundFinished(result: RoundResult) {
        backStack.removeAll { it is Screen.Game }
        _screen.value = Screen.GameOver(result)
    }

    fun goToMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun back() {
        val previous = backStack.removeLastOrNull()
        _screen.value = previous ?: Screen.Menu
    }

    private fun push(next: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash && current !is Screen.GameOver) {
            backStack.add(current)
        }
        _screen.value = next
    }
}
