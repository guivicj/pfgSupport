package org.guivicj.support.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.navigation.Screen
import org.guivicj.support.utils.formatDateTime

@Composable
fun TicketCard(navController: NavHostController, ticketDTO: TicketDTO, user: String) {
    Box(
        modifier = Modifier
            .height(180.dp)
            .padding(bottom = 20.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false,
                spotColor = MaterialTheme.colorScheme.outline
            )
            .clip(RoundedCornerShape((8.dp)))
            .background(MaterialTheme.colorScheme.outline),
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
                Text(
                    text = "Ticket ID: ${ticketDTO.ticketId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,

                    )
                InitialUserProfile(user) {
                    navController.navigate(Screen.FirstOnBoardingScreen.route)
                }
            }
            Text(
                text = "Description: ${ticketDTO.description}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = "Opened at: ${formatDateTime(ticketDTO.openedAt!!)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }

}