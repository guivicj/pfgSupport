package org.guivicj.support.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.domain.model.UserDTO
import org.guivicj.support.ui.core.components.layout.GradientTopBackground
import org.guivicj.support.ui.core.components.texts.TitleText
import org.guivicj.support.ui.screens.home.components.BottomNavMenu
import org.guivicj.support.ui.settings.components.DropdownMenuLanguageSelector
import org.guivicj.support.ui.settings.components.SettingItemCard
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.dark_mode
import support_mobile.composeapp.generated.resources.language
import support_mobile.composeapp.generated.resources.settings_title

@Composable
fun SettingsScreen(
    userDTO: UserDTO,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomNavMenu(
                navController = navController,
                userId = userDTO.id,
                type = userDTO.type,
                selectedRoute = "settings"
            )
        }
    ) {
        GradientTopBackground {
            TitleText(
                text = stringResource(Res.string.settings_title),
                modifier = Modifier.padding(30.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 150.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.padding(8.dp))
            SettingItemCard(
                label = stringResource(Res.string.dark_mode),
                color = Color(0xFF4285F4),
                content = {
                    Switch(checked = isDarkTheme, onCheckedChange = onThemeChange)
                }
            )
            SettingItemCard(
                label = stringResource(Res.string.language),
                color = Color(0xFF0F9D58),
                content = {
                    DropdownMenuLanguageSelector(
                        selected = currentLanguage,
                        onSelected = onLanguageChange
                    )
                }
            )
        }
    }
}
