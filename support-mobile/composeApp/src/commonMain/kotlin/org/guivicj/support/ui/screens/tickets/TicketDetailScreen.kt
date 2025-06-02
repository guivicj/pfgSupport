package org.guivicj.support.ui.screens.tickets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import org.guivicj.support.data.model.UserType
import org.guivicj.support.domain.model.MessageDTO
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.presentation.TechViewModel
import org.guivicj.support.presentation.TicketViewModel
import org.guivicj.support.presentation.UserViewModel
import org.guivicj.support.ui.screens.home.components.TopNavMenu
import org.guivicj.support.ui.screens.tickets.components.MessageBubble
import org.guivicj.support.ui.screens.tickets.components.MessageInputBar
import org.guivicj.support.ui.screens.tickets.components.TicketMetadataSection
import org.guivicj.support.websocket.ChatSyncHandler
import org.guivicj.support.websocket.WebSocketManager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TicketDetailScreen(
    ticketDTO: TicketDTO,
    navController: NavHostController,
    ticketViewModel: TicketViewModel,
    userViewModel: UserViewModel,
    techViewModel: TechViewModel
) {
    val messages by ticketViewModel.messages.collectAsState()
    val currentUser = userViewModel.state.value.id
    val techState by techViewModel.allTechnicians.collectAsState()

    val httpClient = remember {
        HttpClient(CIO) {
            install(WebSockets)
        }
    }

    val handler = remember {
        ChatSyncHandler(WebSocketManager(httpClient))
    }

    LaunchedEffect(ticketDTO.ticketId) {
        ticketViewModel.fetchMessages(ticketDTO.ticketId)
        techViewModel.fetchTechnicians()
    }

    DisposableEffect(ticketDTO.ticketId) {
        handler.connect(ticketDTO.ticketId, currentUser) { message ->
            ticketViewModel.appendMessage(message)
        }

        onDispose {
            handler.disconnect()
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopNavMenu(ticketId = ticketDTO.ticketId, navController = navController)
            },
            bottomBar = {
                MessageInputBar { text ->
                    val message = MessageDTO(
                        ticketId = ticketDTO.ticketId,
                        role = ticketViewModel.getCurrentChatRole(),
                        content = text
                    )
                    ticketViewModel.sendMessage(message)
                    handler.send(message)
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                reverseLayout = false,
            ) {
                stickyHeader {
                    TicketMetadataSection(
                        ticket = ticketDTO,
                        userIsTechnician = userViewModel.state.value.type == UserType.TECHNICIAN,
                        onEscalate = { technicianType ->
                            ticketViewModel.escalateTicketByType(ticketDTO.ticketId, technicianType)
                        },
                        onChangeState = { newState ->
                            ticketViewModel.changeTicketState(
                                ticketDTO.ticketId,
                                newState.name,
                                ticketDTO.technicianId
                            )
                        }
                    )
                }
                itemsIndexed(messages) { index, message ->
                    val previousMessage = messages.getOrNull(index - 1)
                    val sameSender = previousMessage?.role == message.role
                    MessageBubble(
                        message = message,
                        isCurrentUser = message.role == ticketViewModel.getCurrentChatRole(),
                        grouped = sameSender
                    )
                }
            }
        }
    }
}