package org.guivicj.support.ui.screens.tickets.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.presentation.TicketViewModel
import org.guivicj.support.ui.screens.home.components.SearchBar

@Composable
fun TicketList(
    viewModel: TicketViewModel,
    navController: NavHostController,
) {
    Column {
        SearchBar(
            query = viewModel.searchQuery,
            onQueryChange = { viewModel.updateSearch(it) },
            modifier = Modifier
                .height(70.dp)
                .padding(bottom = 20.dp)
        )
        LazyColumn {
            items(viewModel.filteredTickets) { ticket ->
                viewModel.getTicketOwner(ticket)
                val ownerName = viewModel.ticketOwners[ticket.userId] ?: ""
                TicketCard(navController, ticket, ownerName)
            }
        }
    }
}