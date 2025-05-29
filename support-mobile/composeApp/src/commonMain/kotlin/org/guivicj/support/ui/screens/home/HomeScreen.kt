package org.guivicj.support.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import org.guivicj.support.data.model.UserType
import org.guivicj.support.domain.model.UserDTO
import org.guivicj.support.presentation.TicketViewModel
import org.guivicj.support.presentation.UserViewModel
import org.guivicj.support.ui.screens.home.components.BottomNavMenu
import org.guivicj.support.ui.screens.home.components.MainHeader
import org.guivicj.support.ui.screens.tickets.components.CreateTicketBottomSheet
import org.guivicj.support.ui.screens.tickets.components.TicketList
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.home_admin_subtitle
import support_mobile.composeapp.generated.resources.home_tech_subtitle
import support_mobile.composeapp.generated.resources.home_user_subtitle

@Composable
fun HomeScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    ticketViewModel: TicketViewModel
) {
    val userState by userViewModel.state.collectAsState()
    val ticketState by ticketViewModel.state.collectAsState()
    LaunchedEffect(userState) {
        userViewModel.setCurrentUser(
            UserDTO(
                id = userState.id,
                name = userState.name,
                email = userState.email,
                telephone = userState.telephone.toIntOrNull() ?: 0,
                type = userState.type
            )
        )
        ticketViewModel.setUser(userState.id, userState.type)
        ticketViewModel.fetchTickets()
    }

    MaterialTheme {
        Scaffold(
            bottomBar = { BottomNavMenu(navController, userState.id, userState.type, "home") }
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    verticalArrangement = Arrangement.Top,
                ) {
                    val message = ticketState.message
                    LaunchedEffect(message) {
                        if (message != null) {
                            delay(3000)
                            ticketViewModel.clearMessage()
                        }
                    }
                    if (message != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.outline)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                    MainHeader(
                        userState.id,
                        userState.name,
                        showSubtitleForUser(userState.type),
                        navController
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                    ) {
                        TicketList(ticketViewModel, navController)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                FloatingActionButton(
                    onClick = { ticketViewModel.setBottomSheetVisible(true) },
                    modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd),
                    containerColor = MaterialTheme.colorScheme.outline,
                    contentColor = MaterialTheme.colorScheme.background,
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Create Ticket")
                }
                CreateTicketBottomSheet(
                    ticketViewModel = ticketViewModel,
                    visible = ticketViewModel.state.collectAsState().value.showCreateSheet,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun showSubtitleForUser(userType: UserType): String {
    return when (userType) {
        UserType.USER -> stringResource(Res.string.home_user_subtitle)
        UserType.TECHNICIAN -> stringResource(Res.string.home_tech_subtitle)
        UserType.ADMIN -> stringResource(Res.string.home_admin_subtitle)
    }
}
