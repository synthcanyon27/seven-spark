package com.Rirsefu3.Degmed.presentation.levelmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Rirsefu3.Degmed.domain.repository.LevelRepository
import com.Rirsefu3.Degmed.domain.repository.ProgressRepository
import com.Rirsefu3.Degmed.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LevelMapViewModel(
    private val progress: ProgressRepository,
    private val levels: LevelRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelMapUiState())
    val state: StateFlow<LevelMapUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progress.progress.collect { refresh() }
        }
        viewModelScope.launch {
            settings.settings.collect { value ->
                _state.value = _state.value.copy(particlesEnabled = value.particleTrails)
            }
        }
    }

    fun selectTab(tab: MapTab) {
        _state.value = _state.value.copy(tab = tab, lockedMessageVisible = false)
        refresh()
    }

    fun onLockedNode() {
        _state.value = _state.value.copy(lockedMessageVisible = true)
    }

    fun dismissLockedMessage() {
        _state.value = _state.value.copy(lockedMessageVisible = false)
    }

    private fun refresh() {
        val current = _state.value
        val source = if (current.tab == MapTab.SECTORS) levels.sectors() else levels.expertContracts()
        val nodes = source.mapIndexed { index, level ->
            MapNode(
                level = level,
                unlocked = progress.isUnlocked(level),
                stars = progress.starsFor(level.id),
                ordinal = index + 1
            )
        }
        _state.value = current.copy(
            nodes = nodes,
            totalStars = progress.totalStars(),
            maxStars = levels.all().size * 3
        )
    }
}
