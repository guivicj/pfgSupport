package org.guivicj.support.ui.screens.tickets.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.navigation.Screen
import org.guivicj.support.ui.core.components.display.InitialUserProfile
import org.guivicj.support.ui.core.components.texts.TicketText
import org.guivicj.support.utils.formatDateTime
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.open_support_message

@Composable
fun TicketCard(navController: NavHostController, ticketDTO: TicketDTO, user: String) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false,
                spotColor = MaterialTheme.colorScheme.outline
            )
            .clip(RoundedCornerShape((8.dp)))
            .background(MaterialTheme.colorScheme.outline)
            .clickable { expanded = !expanded },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 5.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TicketText("Ticket ID: ${ticketDTO.ticketId}")
                InitialUserProfile(user) {
                    navController.navigate(Screen.ProfileScreen.createRoute(ticketDTO.userId.toString()))
                }
            }
            TicketText("Description: ${ticketDTO.description}")
            TicketText("Opened at: ${formatDateTime(ticketDTO.openedAt!!)}")
            AnimatedVisibility(visible = expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TicketText("Ticket status: ${ticketDTO.state}")

                    ticketDTO.inProgressAt?.let {
                        TicketText("In Progress at: ${formatDateTime(it)}")
                    }

                    ticketDTO.closedAt?.let {
                        TicketText("Closed at: ${formatDateTime(it)}")
                    }

                    Text(
                        text = stringResource(Res.string.open_support_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.clickable {
                            navController.navigate("ticket_detail/${ticketDTO.ticketId}")
                        },
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}
