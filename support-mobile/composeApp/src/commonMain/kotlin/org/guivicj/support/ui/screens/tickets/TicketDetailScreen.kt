package org.guivicj.support.ui.screens.tickets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.guivicj.support.data.model.ChatRole
import org.guivicj.support.domain.model.MessageDTO
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.presentation.TicketViewModel
import org.guivicj.support.ui.screens.home.components.TopNavMenu
import org.guivicj.support.ui.screens.tickets.components.MessageBubble
import org.guivicj.support.ui.screens.tickets.components.MessageInputBar
import org.guivicj.support.ui.screens.tickets.components.TicketMetadataSection

@Composable
fun TicketDetailScreen(
    ticketDTO: TicketDTO,
    navController: NavHostController,
    viewModel: TicketViewModel
) {
    val messages by viewModel.messages.collectAsState()

    LaunchedEffect(ticketDTO.ticketId) {
        viewModel.fetchMessages(ticketDTO.ticketId)
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopNavMenu(ticketId = ticketDTO.ticketId, navController = navController)
            },
            bottomBar = {
                MessageInputBar { message ->
                    viewModel.sendMessage(
                        MessageDTO(
                            ticketId = ticketDTO.ticketId,
                            role = viewModel.getCurrentChatRole(),
                            content = message
                        )
                    )
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
                itemsIndexed(messages) { index, message ->
                    val previousMessage = messages.getOrNull(index - 1)
                    val sameSender = previousMessage?.role == message.role
                    MessageBubble(
                        message = message,
                        isCurrentUser = message.role == viewModel.getCurrentChatRole(),
                        grouped = sameSender
                    )
                }
            }
        }
    }
}