package com.Rirsefu3.Degmed.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Rirsefu3.Degmed.domain.model.RoundResult
import com.Rirsefu3.Degmed.domain.repository.ProgressRepository
import com.Rirsefu3.Degmed.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameOverViewModel(
    result: RoundResult,
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        GameOverUiState(
            result = result,
            totalStars = progress.totalStars(),
            bestSpark = progress.bestSpark.value,
            particlesEnabled = settings.settings.value.particleTrails
        )
    )
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progress.bestSpark.collect { best ->
                _state.value = _state.value.copy(
                    bestSpark = best,
                    totalStars = progress.totalStars()
                )
            }
        }
    }
}
