package org.guivicj.support.ui.screens.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.data.model.UserType
import org.guivicj.support.navigation.Screen
import org.guivicj.support.presentation.TechViewModel
import org.guivicj.support.ui.core.components.layout.GradientTopBackground
import org.guivicj.support.ui.core.components.texts.TitleText
import org.guivicj.support.ui.screens.home.components.BottomNavMenu

@Composable
fun TechListScreen(
    navController: NavHostController,
    viewModel: TechViewModel
) {
    val technicians by viewModel.allTechnicians.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTechnicians()
    }

    Scaffold(
        bottomBar = {
            BottomNavMenu(
                navController = navController,
                userId = 0L,
                type = UserType.ADMIN,
                selectedRoute = Screen.TechListScreen.route
            )
        }
    ) {
        GradientTopBackground {
            TitleText("Technician List", Modifier.padding(30.dp))
        }

        LazyColumn(
            modifier = Modifier
                .padding(top = 160.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize()
        ) {
            items(technicians) { tech ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate(Screen.TechStatsScreen.createRoute(tech.userId))
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Name: ${tech.id}")
                        Text(text = "Type: ${tech.technicianType.name}")
                    }
                }
            }
        }
    }
}
