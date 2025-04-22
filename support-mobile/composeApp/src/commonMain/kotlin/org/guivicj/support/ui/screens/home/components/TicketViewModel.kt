package org.guivicj.support.ui.screens.home.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.guivicj.support.data.model.UserType
import org.guivicj.support.domain.model.TicketDTO

class TicketViewModel : ViewModel() {
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
}