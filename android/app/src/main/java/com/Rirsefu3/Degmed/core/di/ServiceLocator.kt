package com.Rirsefu3.Degmed.core.di

import android.content.Context
import com.Rirsefu3.Degmed.data.local.PreferencesStore
import com.Rirsefu3.Degmed.data.repository.LevelRepositoryImpl
import com.Rirsefu3.Degmed.data.repository.ProgressRepositoryImpl
import com.Rirsefu3.Degmed.data.repository.SettingsRepositoryImpl
import com.Rirsefu3.Degmed.domain.repository.LevelRepository
import com.Rirsefu3.Degmed.domain.repository.ProgressRepository
import com.Rirsefu3.Degmed.domain.repository.SettingsRepository
import com.Rirsefu3.Degmed.domain.usecase.EvaluateBoardUseCase
import com.Rirsefu3.Degmed.domain.usecase.GenerateExpertLevelUseCase
import com.Rirsefu3.Degmed.domain.usecase.ScoreRoundUseCase
import com.Rirsefu3.Degmed.domain.usecase.SpinColumnUseCase

object ServiceLocator {

    val spinColumn = SpinColumnUseCase()
    val evaluateBoard = EvaluateBoardUseCase()
    val scoreRound = ScoreRoundUseCase()

    private var levels: LevelRepository? = null
    private var progressStore: ProgressRepository? = null
    private var settingsStore: SettingsRepository? = null

    fun initialize(context: Context) {
        if (levels != null) return
        val store = PreferencesStore(context)
        val levelRepository = LevelRepositoryImpl(
            GenerateExpertLevelUseCase(evaluateBoard, spinColumn)
        )
        levels = levelRepository
        progressStore = ProgressRepositoryImpl(store, levelRepository.all().map { it.id })
        settingsStore = SettingsRepositoryImpl(store)
    }

    fun levelRepository(): LevelRepository = requireNotNull(levels)

    fun progressRepository(): ProgressRepository = requireNotNull(progressStore)

    fun settingsRepository(): SettingsRepository = requireNotNull(settingsStore)
}
