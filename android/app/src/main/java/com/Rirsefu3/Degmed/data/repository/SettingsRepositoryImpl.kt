package com.Rirsefu3.Degmed.data.repository

import com.Rirsefu3.Degmed.data.local.PreferencesStore
import com.Rirsefu3.Degmed.domain.model.GameSettings
import com.Rirsefu3.Degmed.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val store: PreferencesStore
) : SettingsRepository {

    private val _settings = MutableStateFlow(
        GameSettings(
            numeralsOnTiles = store.readBoolean(PreferencesStore.KEY_NUMERALS, true),
            bandLabels = store.readBoolean(PreferencesStore.KEY_BAND_LABELS, false),
            particleTrails = store.readBoolean(PreferencesStore.KEY_PARTICLES, true)
        )
    )
    override val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    override fun setNumeralsOnTiles(enabled: Boolean) {
        store.writeBoolean(PreferencesStore.KEY_NUMERALS, enabled)
        _settings.value = _settings.value.copy(numeralsOnTiles = enabled)
    }

    override fun setBandLabels(enabled: Boolean) {
        store.writeBoolean(PreferencesStore.KEY_BAND_LABELS, enabled)
        _settings.value = _settings.value.copy(bandLabels = enabled)
    }

    override fun setParticleTrails(enabled: Boolean) {
        store.writeBoolean(PreferencesStore.KEY_PARTICLES, enabled)
        _settings.value = _settings.value.copy(particleTrails = enabled)
    }
}
