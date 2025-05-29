package org.guivicj.support.domain.repository

import com.russhwolf.settings.Settings

class SettingsRepository(private val settings: Settings) {
    companion object {
        private const val THEME_KEY = "dark_theme"
        private const val LANGUAGE_KEY = "language"
    }

    fun isDarkTheme(): Boolean = settings.getBoolean(THEME_KEY, false)
    fun setDarkTheme(value: Boolean) = settings.putBoolean(THEME_KEY, value)

    fun getLanguage(): String = settings.getString(LANGUAGE_KEY, "en")
    fun setLanguage(value: String) = settings.putString(LANGUAGE_KEY, value)
}