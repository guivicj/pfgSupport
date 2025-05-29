package org.guivicj.support


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.guivicj.support.navigation.HomeNav
import org.guivicj.support.presentation.SettingsViewModel
import org.guivicj.support.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(settingsViewModel: SettingsViewModel) {
    val darkTheme by settingsViewModel.darkTheme.collectAsState()
    AppTheme(useDarkTheme = darkTheme) {
        HomeNav()
    }
}