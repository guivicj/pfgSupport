package org.guivicj.support.ui.screens.tickets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.ui.screens.home.components.TopNavMenu
import org.guivicj.support.ui.screens.tickets.components.MessageInputBar
import org.guivicj.support.ui.screens.tickets.components.TicketMetadataSection

@Composable
fun TicketDetailScreen(ticketDTO: TicketDTO, navController: NavHostController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopNavMenu(ticketId = ticketDTO.ticketId, navController = navController)
            },
            bottomBar = {
                MessageInputBar {

                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    TicketMetadataSection(ticketDTO)
                }

//                items() {
                // Message Section
//                }
            }
        }
    }
}