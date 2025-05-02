package org.guivicj.support.ui.screens.tickets

import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.ui.screens.home.components.TopNavMenu
import org.guivicj.support.ui.screens.tickets.components.MessageInputBar

@Composable
fun TicketDetailScreen(ticketDTO: TicketDTO, navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = { TopNavMenu(ticketId = ticketDTO.ticketId, navController = navController) },
            bottomBar = {
                MessageInputBar {

                }
            }
        ) {

        }
    }
}