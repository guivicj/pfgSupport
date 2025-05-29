package org.guivicj.support

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.google.firebase.FirebaseApp
import org.guivicj.support.di.androidModule
import org.guivicj.support.di.initializeKoin
import org.guivicj.support.presentation.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        initializeKoin(androidModule)
        setContent {
            val settingsViewModel = getKoin().get<SettingsViewModel>()
            App(settingsViewModel)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val settingsViewModel = getKoin().get<SettingsViewModel>()
    App(settingsViewModel = settingsViewModel)
}