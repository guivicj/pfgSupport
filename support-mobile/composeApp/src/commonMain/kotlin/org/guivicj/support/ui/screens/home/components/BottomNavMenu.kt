package org.guivicj.support.ui.screens.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.data.model.UserType
import org.guivicj.support.navigation.Screen

@Composable
fun BottomNavMenu(
    navController: NavHostController,
    userId: Long?,
    type: UserType,
    selectedRoute: String
) {
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        val items = when (type) {
            UserType.ADMIN -> listOf(
                "home" to Icons.Default.Home,
                "tech-list" to Icons.Default.Person,
                "analytics" to Icons.Default.Info,
                "settings" to Icons.Default.Settings
            )

            UserType.TECHNICIAN -> listOf(
                "home" to Icons.Default.Home,
                "your-tickets" to Icons.Default.Email,
                "analytics" to Icons.Default.Info,
                "settings" to Icons.Default.Settings
            )

            UserType.USER -> listOf(
                "home" to Icons.Default.Home,
                "settings" to Icons.Default.Settings
            )
        }

        items.forEach { (route, icon) ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        icon, contentDescription = route,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                selected = route == selectedRoute,
                onClick = {
                    if (route == "analytics" && type == UserType.TECHNICIAN) {
                        val techId = userId ?: return@BottomNavigationItem
                        navController.navigate(Screen.TechStatsScreen.createRoute(techId)) {
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
