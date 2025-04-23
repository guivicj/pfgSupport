package org.guivicj.support.ui.screens.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.cancel_btn
import support_mobile.composeapp.generated.resources.create_ticket_btn
import support_mobile.composeapp.generated.resources.description_field
import support_mobile.composeapp.generated.resources.failed_ticket_message
import support_mobile.composeapp.generated.resources.product_field
import support_mobile.composeapp.generated.resources.success_ticket_message

@Composable
fun CreateTicketBottomSheet(
    ticketViewModel: TicketViewModel,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    val ticketState by ticketViewModel.state.collectAsState()
    val successMessage = stringResource(Res.string.success_ticket_message)
    val errorMessage = stringResource(Res.string.failed_ticket_message)
    AnimatedVisibility(visible = visible, modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(50.dp)
                .height(450.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = ticketState.description,
                    onValueChange = { ticketViewModel.onDescriptionChange(it) },
                    singleLine = true,
                    placeholder = { stringResource(Res.string.description_field) }
                )

                Spacer(modifier = Modifier.padding(24.dp))

                DropdownField(
                    label = stringResource(Res.string.product_field),
                    value = ticketState.product.toString(),
                    onOptionSelected = { ticketViewModel.onProductChange(it) },
                    options = ticketState.products.map { it },
                    expanded = ticketState.expandProductDropdown,
                    isError = ticketState.productError != null,
                    onExpandedChange = { ticketViewModel.setProductDropdownExpanded(it) }
                )

                Spacer(modifier = Modifier.padding(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TicketCreationButton(
                        label = stringResource(Res.string.create_ticket_btn),
                        onClick = {
                            val state = ticketState
                            ticketViewModel.createTicket(
                                productId = state.productId,
                                description = state.description,
                                onSuccess = {
                                    ticketViewModel.setBottomSheetVisible(false)
                                    ticketViewModel.onDescriptionChange("")
                                    ticketViewModel.showMessage(successMessage)
                                },
                                onError = { ticketViewModel.showMessage("Error: ${it.message}") }
                            )
                        })
                    TicketCreationButton(
                        label = stringResource(Res.string.cancel_btn),
                        onClick = {
                            ticketViewModel.onDescriptionChange("")
                            ticketViewModel.setBottomSheetVisible(false)
                        }
                    )
                }
            }
        }
    }
}

