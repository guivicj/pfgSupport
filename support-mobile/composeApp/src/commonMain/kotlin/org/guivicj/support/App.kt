package org.guivicj.support


import androidx.compose.runtime.Composable
import org.guivicj.support.navigation.HomeNav
import org.guivicj.support.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        HomeNav()
    }
}