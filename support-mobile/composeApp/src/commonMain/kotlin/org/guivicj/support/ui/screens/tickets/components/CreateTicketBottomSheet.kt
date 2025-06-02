package org.guivicj.support.ui.screens.tickets.components

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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.guivicj.support.data.model.ProductType
import org.guivicj.support.presentation.TicketViewModel
import org.guivicj.support.ui.screens.home.components.DropdownField
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.cancel_btn
import support_mobile.composeapp.generated.resources.create_ticket_btn
import support_mobile.composeapp.generated.resources.create_ticket_subtitle
import support_mobile.composeapp.generated.resources.description_field
import support_mobile.composeapp.generated.resources.product_field

@Composable
fun CreateTicketBottomSheet(
    ticketViewModel: TicketViewModel,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    val ticketState by ticketViewModel.state.collectAsState()
    AnimatedVisibility(visible = visible, modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(50.dp)
                .height(450.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.create_ticket_btn),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.padding(12.dp))

                Text(
                    text = stringResource(Res.string.create_ticket_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.padding(24.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = ticketState.description,
                    onValueChange = { ticketViewModel.onDescriptionChange(it) },
                    singleLine = true,
                    label = {
                        Text(
                            stringResource(Res.string.description_field),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    placeholder = { Text(stringResource(Res.string.description_field)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
                )

                Spacer(modifier = Modifier.padding(24.dp))

                DropdownField(
                    label = stringResource(Res.string.product_field),
                    value = ticketState.product.toString(),
                    onOptionSelected = { ticketViewModel.onProductChange(it as ProductType) },
                    options = ticketState.products.map { it },
                    expanded = ticketState.expandProductDropdown,
                    isError = ticketState.productError != null,
                    onExpandedChange = { ticketViewModel.setProductDropdownExpanded(it) }
                )

                Spacer(modifier = Modifier.padding(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                                    ticketViewModel.showMessage("Ticket created successfully")
                                    ticketViewModel.onDescriptionChange("")
                                    ticketViewModel.setBottomSheetVisible(false)
                                },
                                onError = {
                                    ticketViewModel.showMessage("Failed to create ticket")
                                }
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    TicketCreationButton(
                        label = stringResource(Res.string.cancel_btn),
                        onClick = {
                            ticketViewModel.onDescriptionChange("")
                            ticketViewModel.setBottomSheetVisible(false)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

