package org.guivicj.support.ui.screens.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.guivicj.support.ui.core.components.layout.GradientTopBackground
import org.guivicj.support.ui.core.components.texts.TitleText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavMenu(ticketId: Long, navController: NavHostController) {
    GradientTopBackground {
        TopAppBar(
            title = { TitleText("Ticket #$ticketId") },
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            }
        )
    }
}