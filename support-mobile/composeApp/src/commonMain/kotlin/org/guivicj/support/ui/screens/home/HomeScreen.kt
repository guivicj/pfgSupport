package org.guivicj.support.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.datetime.LocalDateTime
import org.guivicj.support.data.model.StateType
import org.guivicj.support.data.model.UserType
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.ui.screens.home.components.MainHeader
import org.guivicj.support.ui.screens.home.components.SearchBar
import org.guivicj.support.ui.screens.home.components.TicketCard
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.home_admin_subtitle
import support_mobile.composeapp.generated.resources.home_tech_subtitle
import support_mobile.composeapp.generated.resources.home_user_subtitle

@Composable
fun HomeScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val userState by userViewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }
    MaterialTheme {
        Scaffold {
            Column(
                verticalArrangement = Arrangement.Top,
            ) {
                MainHeader(
                    userState.name,
                    showSubtitleForUser(userState.type),
                    navController
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                ) {
                    SearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                TicketCard(
                    navController,
                    TicketDTO(
                        ticketId = 1,
                        productId = 1,
                        technicianId = 1,
                        userId = 52,
                        description = "Ticket 1",
                        state = StateType.OPEN,
                        openedAt = LocalDateTime(2023, 1, 1, 1, 1),
                        inProgressAt = LocalDateTime(2023, 1, 1, 1, 1),
                        closedAt = LocalDateTime(2023, 1, 1, 1, 1),
                    ),
                    user = userState.name
                )
            }
        }
    }
}

@Composable
private fun showSubtitleForUser(userType: UserType): String {
    return when (userType) {
        UserType.USER -> stringResource(Res.string.home_user_subtitle)
        UserType.TECHNICIAN -> stringResource(Res.string.home_tech_subtitle)
        UserType.ADMIN -> stringResource(Res.string.home_admin_subtitle)
    }
}
