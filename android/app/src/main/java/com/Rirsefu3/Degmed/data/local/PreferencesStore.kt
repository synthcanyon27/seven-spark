package com.Rirsefu3.Degmed.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun readBoolean(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun writeBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun clearPrefix(prefix: String) {
        val editor = prefs.edit()
        prefs.all.keys.filter { it.startsWith(prefix) }.forEach { editor.remove(it) }
        editor.apply()
    }

    companion object {
        private const val STORE_NAME = "seven_spark_store"
        const val KEY_STARS = "stars_"
        const val KEY_LEVEL_SPARK = "level_spark_"
        const val KEY_TOTAL_SPARK = "total_spark"
        const val KEY_BEST_SPARK = "best_spark"
        const val KEY_NUMERALS = "opt_numerals"
        const val KEY_BAND_LABELS = "opt_band_labels"
        const val KEY_PARTICLES = "opt_particles"
    }
}
