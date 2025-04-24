package org.guivicj.support.ui.screens.home.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.data.model.ProductType
import org.guivicj.support.data.model.StateType
import org.guivicj.support.data.model.UserType
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.domain.repository.TicketRepository
import org.guivicj.support.firebase.FirebaseTokenProvider

class TicketViewModel(
    private val ticketRepository: TicketRepository,
    private val tokenProvider: FirebaseTokenProvider

) : ViewModel() {
    private val _state = MutableStateFlow(TicketUIState())
    val state = _state.asStateFlow()
    private var allTickets by mutableStateOf(listOf<TicketDTO>())
    var searchQuery by mutableStateOf("")
    private var userType by mutableStateOf(UserType.USER)
    private var currentUserId by mutableStateOf(0L)

    val filteredTickets: List<TicketDTO>
        get() = allTickets.filter { ticket ->
            val matchesSearch = ticket.description.contains(searchQuery, ignoreCase = true)
            val matchesRole = when (userType) {
                UserType.ADMIN -> true
                UserType.TECHNICIAN -> ticket.technicianId == currentUserId
                UserType.USER -> ticket.userId == currentUserId
            }
            matchesSearch && matchesRole
        }

    fun updateSearch(query: String) {
        searchQuery = query
    }

    fun setUser(userId: Long, type: UserType) {
        currentUserId = userId
        userType = type
    }

    fun loadTickets(tickets: List<TicketDTO>) {
        allTickets = tickets
    }

    fun fetchTickets() {
        viewModelScope.launch {
            try {
                val tickets = ticketRepository.getByUser(currentUserId)
                allTickets = tickets
            } catch (e: Exception) {
                showMessage("Failed to load tickets: ${e.message}")
            }
        }
    }

    fun createTicket(
        productId: Long,
        description: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = tokenProvider.getIdToken()
                val dto = TicketDTO(
                    ticketId = 0,
                    userId = 0,
                    technicianId = 0,
                    productId = productId,
                    description = description,
                    state = StateType.OPEN
                )
                val created = ticketRepository.create(dto, token)
                allTickets = allTickets + created
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun onDescriptionChange(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    fun onProductChange(product: ProductType) {
        _state.value = _state.value.copy(
            product = product,
            productId = when (product) {
                ProductType.ENGINE -> 1L
                ProductType.BATTERY -> 2L
            }
        )
    }


    fun setBottomSheetVisible(visible: Boolean) {
        _state.value = _state.value.copy(showCreateSheet = visible)
    }

    fun setProductDropdownExpanded(expanded: Boolean) {
        _state.value = _state.value.copy(expandProductDropdown = expanded)
    }


    fun showMessage(text: String) {
        _state.value = _state.value.copy(message = text)
    }

    fun clearMessage() {
        _state.value = _state.value.copy(message = null)
    }
}

data class TicketUIState(
    val ticketId: Long = 0L,
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