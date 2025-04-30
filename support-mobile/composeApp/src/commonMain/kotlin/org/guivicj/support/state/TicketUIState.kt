package org.guivicj.support.state

import org.guivicj.support.data.model.ProductType
import org.guivicj.support.data.model.StateType

data class TicketUIState(
    val ticketId: Long = 0L,
    val userOwner: String = "",
    val userId: Long = 0L,
    val technicianId: Long = 0L,
    val productId: Long = 0L,
    val description: String = "",
    val product: ProductType = ProductType.ENGINE,
    val showCreateSheet: Boolean = false,
    val expandProductDropdown: Boolean = false,
    val products: List<ProductType> = ProductType.entries,
    val productError: String? = null,
    val state: StateType = StateType.OPEN,
    val message: String? = null,
)