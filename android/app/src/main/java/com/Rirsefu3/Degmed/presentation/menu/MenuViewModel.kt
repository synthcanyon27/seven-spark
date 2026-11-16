package com.Rirsefu3.Degmed.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Rirsefu3.Degmed.domain.model.GameSettings
import com.Rirsefu3.Degmed.domain.model.LevelProgress
import com.Rirsefu3.Degmed.domain.repository.LevelRepository
import com.Rirsefu3.Degmed.domain.repository.ProgressRepository
import com.Rirsefu3.Degmed.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository,
    private val levels: LevelRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState(maxStars = levels.all().size * 3))
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                progress.progress,
                progress.totalSpark,
                progress.bestSpark,
                settings.settings
            ) { levelProgress, totalSpark, bestSpark, gameSettings ->
                Snapshot(levelProgress, totalSpark, bestSpark, gameSettings)
            }.collect { snapshot ->
                applySnapshot(snapshot)
            }
        }
    }

    private data class Snapshot(
        val levelProgress: Map<Int, LevelProgress>,
        val totalSpark: Int,
        val bestSpark: Int,
        val settings: GameSettings
    )

    private fun applySnapshot(snapshot: Snapshot) {
        _state.value = _state.value.copy(
            totalStars = snapshot.levelProgress.values.sumOf { it.stars },
            maxStars = levels.all().size * 3,
            bestSpark = snapshot.bestSpark,
            totalSpark = snapshot.totalSpark,
            currentLevelId = resolveCurrentLevel(snapshot.levelProgress),
            settings = snapshot.settings
        )
    }

    private fun resolveCurrentLevel(levelProgress: Map<Int, LevelProgress>): Int {
        val sectors = levels.sectors()
        val pending = sectors.firstOrNull { (levelProgress[it.id]?.stars ?: 0) == 0 }
        return pending?.id ?: sectors.last().id
    }

    fun openSettings() {
        _state.value = _state.value.copy(settingsVisible = true)
    }

    fun closeSettings() {
        _state.value = _state.value.copy(settingsVisible = false, resetConfirmVisible = false)
    }

    fun setNumerals(enabled: Boolean) {
        settings.setNumeralsOnTiles(enabled)
    }

    fun setBandLabels(enabled: Boolean) {
        settings.setBandLabels(enabled)
    }

    fun setParticles(enabled: Boolean) {
        settings.setParticleTrails(enabled)
    }

    fun requestReset() {
        _state.value = _state.value.copy(resetConfirmVisible = true)
    }

    fun dismissReset() {
        _state.value = _state.value.copy(resetConfirmVisible = false)
    }

    fun confirmReset() {
        progress.resetAll()
        _state.value = _state.value.copy(resetConfirmVisible = false)
    }
}
