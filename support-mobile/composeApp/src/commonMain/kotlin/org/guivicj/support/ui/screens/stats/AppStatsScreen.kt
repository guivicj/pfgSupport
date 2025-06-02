package org.guivicj.support.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Briefcase
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Hourglass
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Users
import org.guivicj.support.data.model.UserType
import org.guivicj.support.navigation.Screen
import org.guivicj.support.presentation.TechViewModel
import org.guivicj.support.ui.core.components.layout.GradientTopBackground
import org.guivicj.support.ui.core.components.texts.TitleText
import org.guivicj.support.ui.screens.home.components.BottomNavMenu
import org.guivicj.support.ui.screens.stats.components.StatCard
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.avg_tickets
import support_mobile.composeapp.generated.resources.closed_tickets
import support_mobile.composeapp.generated.resources.in_progress_tickets
import support_mobile.composeapp.generated.resources.opened_tickets
import support_mobile.composeapp.generated.resources.total_techs

@Composable
fun AppStatsScreen(viewModel: TechViewModel, navController: NavHostController) {
    val stats by viewModel.adminStats.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAdminStats()
    }

    Scaffold(
        bottomBar = {
            BottomNavMenu(
                navController = navController,
                userId = 0L,
                type = UserType.ADMIN,
                selectedRoute = Screen.AppStatsScreen.route
            )
        }
    ) {
        GradientTopBackground {
            TitleText(text = "App Analytics", modifier = Modifier.padding(30.dp))
        }

        stats?.let {
            Column(
                modifier = Modifier
                    .padding(top = 160.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                StatCard(
                    stringResource(Res.string.opened_tickets),
                    it.opened.toString(),
                    Lucide.Briefcase,
                    MaterialTheme.colorScheme.primary
                )
                StatCard(
                    stringResource(Res.string.in_progress_tickets),
                    it.inProgress.toString(),
                    Lucide.Hourglass,
                    MaterialTheme.colorScheme.secondary
                )
                StatCard(
                    stringResource(Res.string.closed_tickets),
                    it.closed.toString(),
                    Lucide.CircleCheck,
                    MaterialTheme.colorScheme.tertiary
                )
                StatCard(
                    stringResource(Res.string.avg_tickets),
                    "${it.avgResolutionTimeMinutes} min",
                    Lucide.Clock,
                    MaterialTheme.colorScheme.outline
                )
                StatCard(
                    stringResource(Res.string.total_techs),
                    it.totalTechnicians.toString(),
                    Lucide.Users,
                    MaterialTheme.colorScheme.primary
                )
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
