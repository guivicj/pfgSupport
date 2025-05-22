package org.guivicj.support.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.data.model.ChatRole
import org.guivicj.support.data.model.ProductType
import org.guivicj.support.data.model.StateType
import org.guivicj.support.data.model.UserType
import org.guivicj.support.domain.model.MessageDTO
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.domain.repository.TicketRepository
import org.guivicj.support.domain.repository.UserRepository
import org.guivicj.support.firebase.FirebaseTokenProvider
import org.guivicj.support.state.TicketUIState

class TicketViewModel(
    private val ticketRepository: TicketRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: FirebaseTokenProvider

) : ViewModel() {
    private val _state = MutableStateFlow(TicketUIState())
    val state = _state.asStateFlow()
    private var allTickets by mutableStateOf(listOf<TicketDTO>())
    private val _selectedTicker = MutableStateFlow<TicketDTO?>(null)
    val selectedTicket = _selectedTicker.asStateFlow()
    private val _messages = MutableStateFlow<List<MessageDTO>>(emptyList())
    val messages = _messages.asStateFlow()
    var searchQuery by mutableStateOf("")
    private var userType by mutableStateOf(UserType.USER)
    private var currentUserId by mutableStateOf(0L)
    val ticketOwners = mutableStateMapOf<Long, String>()

    val filteredTickets: List<TicketDTO>
        get() = allTickets.filter { ticket ->
            val matchesSearch = ticket.description.contains(searchQuery, ignoreCase = true)
            val matchesRole = when (userType) {
                UserType.ADMIN, UserType.TECHNICIAN -> true
                UserType.USER -> ticket.userId == currentUserId
            }
            matchesSearch && matchesRole
        }

    fun getCurrentChatRole(): ChatRole {
        return when (userType) {
            UserType.USER -> ChatRole.USER
            else -> ChatRole.TECHNICIAN
        }
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

    fun getTicketOwner(ticket: TicketDTO) {
        if (ticketOwners.containsKey(ticket.userId)) return
        viewModelScope.launch {
            try {
                val owner = userRepository.getUserById(ticket.userId)
                ticketOwners[ticket.userId] = owner.name
            } catch (e: Exception) {
                showMessage("Failed to load ticket owner: ${e.message}")
            }
        }
    }

    fun fetchTickets() {
        viewModelScope.launch {
            try {
                val tickets = when (userType) {
                    UserType.ADMIN, UserType.TECHNICIAN -> ticketRepository.getAll()
                    UserType.USER -> ticketRepository.getByUser(currentUserId)
                }
                allTickets = tickets
            } catch (e: Exception) {
                showMessage("Failed to load tickets: ${e.message}")
            }
        }
    }

    fun fetchTicketsByTechnician(techId: Long) {
        viewModelScope.launch {
            try {
                val tickets = when (userType) {
                    UserType.TECHNICIAN -> ticketRepository.getByTechnician(techId)
                    else -> emptyList()
                }
                allTickets = tickets
            } catch (e: Exception) {
                showMessage("Failed to load tickets: ${e.message}")
            }

        }
    }

    fun fetchTicketById(ticketId: String) {
        viewModelScope.launch {
            try {
                val ticket = ticketRepository.getById(ticketId.toLong())
                _selectedTicker.value = ticket
            } catch (e: Exception) {
                showMessage("Failed to load ticket: ${e.message}")
            }
        }
    }

    fun fetchMessages(ticketId: Long) {
        viewModelScope.launch {
            try {
                val token = tokenProvider.getIdToken()
                val messages = ticketRepository.getMessages(ticketId, token)
                _messages.value = messages
            } catch (e: Exception) {
                showMessage("Failed to load messages: ${e.message}")
            }
        }
    }

    fun sendMessage(messageDTO: MessageDTO) {
        viewModelScope.launch {
            try {
                val token = tokenProvider.getIdToken()
                ticketRepository.sendMessage(
                    ticketId = messageDTO.ticketId,
                    role = messageDTO.role,
                    content = messageDTO.content,
                    idToken = token
                )
            } catch (e: Exception) {
                showMessage("Failed to send message: ${e.message}")
            }
        }
    }

    fun appendMessage(message: MessageDTO) {
        _messages.value += message
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
