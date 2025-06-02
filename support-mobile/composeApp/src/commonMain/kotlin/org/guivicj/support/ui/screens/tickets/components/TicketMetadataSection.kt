package org.guivicj.support.ui.screens.tickets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.guivicj.support.data.model.StateType
import org.guivicj.support.data.model.TechnicianType
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.ui.screens.home.components.DropdownField
import org.guivicj.support.utils.formatDateTime
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.change_state
import support_mobile.composeapp.generated.resources.closed_at_field
import support_mobile.composeapp.generated.resources.escalate_ticket
import support_mobile.composeapp.generated.resources.in_progress_at_field
import support_mobile.composeapp.generated.resources.opened_at_field
import support_mobile.composeapp.generated.resources.status_field

@Composable
fun TicketMetadataSection(
    ticket: TicketDTO,
    userIsTechnician: Boolean,
    onEscalate: (TechnicianType) -> Unit,
    onChangeState: (StateType) -> Unit
) {
    var selectedType by remember { mutableStateOf(TechnicianType.JUNIOR) }
    var isEscalationExpanded by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    var selectedState by remember { mutableStateOf(StateType.IN_PROGRESS) }
    var isStateExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(enabled = userIsTechnician) { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        TicketInfoRow(stringResource(Res.string.status_field), ticket.state.toString())
        TicketInfoRow(stringResource(Res.string.opened_at_field), formatDateTime(ticket.openedAt.toString()))
        ticket.inProgressAt?.let {
            TicketInfoRow(stringResource(Res.string.in_progress_at_field), formatDateTime(it))
        }
        ticket.closedAt?.let {
            TicketInfoRow(stringResource(Res.string.closed_at_field), formatDateTime(it))
        }

        if (userIsTechnician && isExpanded) {
            Spacer(modifier = Modifier.height(12.dp))
            DropdownField(
                label = stringResource(Res.string.escalate_ticket),
                value = selectedType.name,
                isError = false,
                expanded = isEscalationExpanded,
                options = TechnicianType.entries,
                onExpandedChange = { isEscalationExpanded = it },
                onOptionSelected = {
                    selectedType = it as TechnicianType
                    onEscalate(it)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            DropdownField(
                label = stringResource(Res.string.change_state),
                value = selectedState.name,
                isError = false,
                expanded = isStateExpanded,
                options = StateType.entries,
                onExpandedChange = { isStateExpanded = it },
                onOptionSelected = {
                    selectedState = it as StateType
                    onChangeState(it)
                }
            )
        }
    }
}
