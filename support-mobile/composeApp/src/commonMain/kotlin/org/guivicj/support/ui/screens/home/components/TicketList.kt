package org.guivicj.support.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun TicketList(
    viewModel: TicketViewModel,
    navController: NavHostController,
    username: String
) {
    Column {
        SearchBar(
            query = viewModel.searchQuery,
            onQueryChange = { viewModel.updateSearch(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        LazyColumn {
            items(viewModel.filteredTickets) { ticket ->
                TicketCard(navController, ticket, username)
            }
        }
    }
}