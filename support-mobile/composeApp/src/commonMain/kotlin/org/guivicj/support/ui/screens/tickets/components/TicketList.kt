package org.guivicj.support.ui.screens.tickets.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.data.model.StateType
import org.guivicj.support.presentation.TicketViewModel
import org.guivicj.support.ui.screens.home.components.SearchBar

@OptIn(ExperimentalLayoutApi::class)
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
                .padding(bottom = 8.dp)
        )

        val filters = listOf(null) + StateType.entries
        val selectedFilter = viewModel.selectedStateFilter

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 4.dp),
            maxLines = 1,
        ) {
            filters.forEach { state ->
                val selected = state == selectedFilter
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.updateStateFilter(state) },
                    label = { Text(state?.name ?: "ALL") },
                    modifier = Modifier.padding(start = 1.5.dp, end = 1.5.dp)
                )
            }
        }

        LazyColumn {
            items(viewModel.filteredTickets) { ticket ->
                viewModel.getTicketOwner(ticket)
                val ownerName = viewModel.ticketOwners[ticket.userId] ?: ""
                TicketCard(navController, ticket, ownerName)
            }
        }
    }
}
