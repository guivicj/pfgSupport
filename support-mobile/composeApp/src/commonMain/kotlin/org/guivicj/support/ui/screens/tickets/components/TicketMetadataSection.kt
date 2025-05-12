package org.guivicj.support.ui.screens.tickets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.guivicj.support.domain.model.TicketDTO
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.closed_at_field
import support_mobile.composeapp.generated.resources.in_progress_at_field
import support_mobile.composeapp.generated.resources.opened_at_field
import support_mobile.composeapp.generated.resources.status_field

@Composable
fun TicketMetadataSection(ticket: TicketDTO) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        TicketInfoRow(stringResource(Res.string.status_field), ticket.state.toString())
        TicketInfoRow(stringResource(Res.string.opened_at_field), ticket.openedAt.toString())
        ticket.inProgressAt?.let {
            TicketInfoRow(stringResource(Res.string.in_progress_at_field), it)
        }
        ticket.closedAt?.let {
            TicketInfoRow(stringResource(Res.string.closed_at_field), it)
        }
    }
}