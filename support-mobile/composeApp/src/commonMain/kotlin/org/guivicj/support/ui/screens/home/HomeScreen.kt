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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.data.model.UserType
import org.guivicj.support.ui.screens.home.components.MainHeader
import org.guivicj.support.ui.screens.home.components.TicketList
import org.guivicj.support.ui.screens.home.components.TicketViewModel
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.home_admin_subtitle
import support_mobile.composeapp.generated.resources.home_tech_subtitle
import support_mobile.composeapp.generated.resources.home_user_subtitle

@Composable
fun HomeScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    ticketViewModel: TicketViewModel
) {
    val userState by userViewModel.state.collectAsState()
    ticketViewModel.setUser(userState.id, userState.type)
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
                    TicketList(ticketViewModel, navController, userState.name)
                }
                Spacer(modifier = Modifier.height(24.dp))
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
