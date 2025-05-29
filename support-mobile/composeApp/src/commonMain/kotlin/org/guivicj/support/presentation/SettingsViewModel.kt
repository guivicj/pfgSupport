package org.guivicj.support.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.guivicj.support.domain.repository.SettingsRepository

class SettingsViewModel(private val repo: SettingsRepository) {

    private val _darkTheme = MutableStateFlow(repo.isDarkTheme())
    val darkTheme: StateFlow<Boolean> = _darkTheme

    private val _language = MutableStateFlow(repo.getLanguage())
    val language: StateFlow<String> = _language

    fun toggleTheme() {
        val newValue = !_darkTheme.value
        _darkTheme.value = newValue
        repo.setDarkTheme(newValue)
    }

    fun changeLanguage(newLang: String) {
        _language.value = newLang
        repo.setLanguage(newLang)
    }
}
