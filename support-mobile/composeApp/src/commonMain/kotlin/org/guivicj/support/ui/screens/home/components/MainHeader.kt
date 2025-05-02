package org.guivicj.support.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.navigation.Screen
import org.guivicj.support.ui.core.components.texts.SubtitleText
import org.guivicj.support.ui.core.components.texts.TitleText
import org.guivicj.support.ui.core.components.layout.GradientTopBackground
import org.guivicj.support.ui.core.components.display.InitialUserProfile

@Composable
fun MainHeader(id: Long, user: String, subtitle: String, navController: NavHostController) {
    GradientTopBackground {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                TitleText("Hi, $user!")
                SubtitleText(subtitle)
            }

            InitialUserProfile(user) {
                navController.navigate(Screen.ProfileScreen.createRoute(id.toString()))
            }
        }
    }
}
