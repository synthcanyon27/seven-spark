package com.Rirsefu3.Degmed.domain.repository

import com.Rirsefu3.Degmed.domain.model.GameSettings
import com.Rirsefu3.Degmed.domain.model.LevelDefinition
import com.Rirsefu3.Degmed.domain.model.LevelProgress
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<Map<Int, LevelProgress>>
    val totalSpark: StateFlow<Int>
    val bestSpark: StateFlow<Int>
    fun totalStars(): Int
    fun starsFor(levelId: Int): Int
    fun isUnlocked(level: LevelDefinition): Boolean
    fun recordRound(levelId: Int, stars: Int, spark: Int): Boolean
    fun resetAll()
}

interface SettingsRepository {
    val settings: StateFlow<GameSettings>
    fun setNumeralsOnTiles(enabled: Boolean)
    fun setBandLabels(enabled: Boolean)
    fun setParticleTrails(enabled: Boolean)
}

interface LevelRepository {
    fun sectors(): List<LevelDefinition>
    fun expertContracts(): List<LevelDefinition>
    fun all(): List<LevelDefinition>
    fun level(id: Int, attempt: Int): LevelDefinition
    fun nextLevelId(id: Int): Int?
}
