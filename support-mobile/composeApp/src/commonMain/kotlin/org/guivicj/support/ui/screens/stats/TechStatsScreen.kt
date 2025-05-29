package org.guivicj.support.ui.screens.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.data.model.UserType
import org.guivicj.support.presentation.TechAnalyticsViewModel
import org.guivicj.support.state.TechAnalyticsState
import org.guivicj.support.ui.core.components.layout.GradientTopBackground
import org.guivicj.support.ui.core.components.texts.TitleText
import org.guivicj.support.ui.screens.home.components.BottomNavMenu
import org.guivicj.support.ui.screens.stats.components.StatsContent
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.stats_performance

@Composable
fun TechStatsScreen(
    technicianId: Long,
    viewModel: TechAnalyticsViewModel,
    navController: NavHostController
) {
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.loadStats(technicianId)
    }

    Scaffold(bottomBar = {
        BottomNavMenu(
            navController = navController,
            userId = technicianId,
            type = UserType.TECHNICIAN,
            selectedRoute = "analytics"
        )
    }) {
        GradientTopBackground {
            TitleText(
                text = stringResource(Res.string.stats_performance),
                modifier = Modifier.padding(30.dp)
            )
        }
        when (state) {
            is TechAnalyticsState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is TechAnalyticsState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}")
                }
            }

            is TechAnalyticsState.Success -> {
                StatsContent(stats = state.stats, modifier = Modifier.padding(top = 150.dp))
            }
        }
    }
}