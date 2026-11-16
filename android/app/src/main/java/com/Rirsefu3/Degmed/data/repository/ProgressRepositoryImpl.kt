package com.Rirsefu3.Degmed.data.repository

import com.Rirsefu3.Degmed.data.local.PreferencesStore
import com.Rirsefu3.Degmed.domain.model.LevelDefinition
import com.Rirsefu3.Degmed.domain.model.LevelProgress
import com.Rirsefu3.Degmed.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(
    private val store: PreferencesStore,
    private val levelIds: List<Int>
) : ProgressRepository {

    private val _progress = MutableStateFlow(load())
    override val progress: StateFlow<Map<Int, LevelProgress>> = _progress.asStateFlow()

    private val _totalSpark = MutableStateFlow(store.readInt(PreferencesStore.KEY_TOTAL_SPARK, 0))
    override val totalSpark: StateFlow<Int> = _totalSpark.asStateFlow()

    private val _bestSpark = MutableStateFlow(store.readInt(PreferencesStore.KEY_BEST_SPARK, 0))
    override val bestSpark: StateFlow<Int> = _bestSpark.asStateFlow()

    private fun load(): Map<Int, LevelProgress> = levelIds.associateWith { id ->
        LevelProgress(
            levelId = id,
            stars = store.readInt(PreferencesStore.KEY_STARS + id, 0),
            bestSpark = store.readInt(PreferencesStore.KEY_LEVEL_SPARK + id, 0)
        )
    }

    override fun totalStars(): Int = _progress.value.values.sumOf { it.stars }

    override fun starsFor(levelId: Int): Int = _progress.value[levelId]?.stars ?: 0

    override fun isUnlocked(level: LevelDefinition): Boolean = when {
        level.expert -> totalStars() >= level.starsRequired
        level.id <= FIRST_LEVEL_ID -> true
        else -> starsFor(level.id - 1) > 0
    }

    override fun recordRound(levelId: Int, stars: Int, spark: Int): Boolean {
        val current = _progress.value[levelId] ?: LevelProgress(levelId, 0, 0)
        val nextStars = maxOf(current.stars, stars)
        val nextLevelSpark = maxOf(current.bestSpark, spark)
        store.writeInt(PreferencesStore.KEY_STARS + levelId, nextStars)
        store.writeInt(PreferencesStore.KEY_LEVEL_SPARK + levelId, nextLevelSpark)
        _progress.value = _progress.value + (levelId to LevelProgress(levelId, nextStars, nextLevelSpark))

        val nextTotal = _totalSpark.value + spark
        store.writeInt(PreferencesStore.KEY_TOTAL_SPARK, nextTotal)
        _totalSpark.value = nextTotal

        val isRecord = spark > _bestSpark.value
        if (isRecord) {
            store.writeInt(PreferencesStore.KEY_BEST_SPARK, spark)
            _bestSpark.value = spark
        }
        return isRecord
    }

    override fun resetAll() {
        store.clearPrefix(PreferencesStore.KEY_STARS)
        store.clearPrefix(PreferencesStore.KEY_LEVEL_SPARK)
        store.writeInt(PreferencesStore.KEY_TOTAL_SPARK, 0)
        store.writeInt(PreferencesStore.KEY_BEST_SPARK, 0)
        _progress.value = load()
        _totalSpark.value = 0
        _bestSpark.value = 0
    }

    companion object {
        const val FIRST_LEVEL_ID = 1
    }
}
