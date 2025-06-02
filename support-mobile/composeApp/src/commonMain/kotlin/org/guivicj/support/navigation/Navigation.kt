package org.guivicj.support.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.guivicj.support.presentation.ForgotPasswordViewModel
import org.guivicj.support.presentation.SettingsViewModel
import org.guivicj.support.presentation.TechAnalyticsViewModel
import org.guivicj.support.presentation.TechViewModel
import org.guivicj.support.presentation.TicketViewModel
import org.guivicj.support.presentation.UserViewModel
import org.guivicj.support.ui.screens.home.HomeScreen
import org.guivicj.support.ui.screens.onboarding.FirstOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.FourthOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.SecondOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.ThirdOnBoardingScreen
import org.guivicj.support.ui.screens.profile.ProfileScreen
import org.guivicj.support.ui.screens.signin.ForgotPasswordScreen
import org.guivicj.support.ui.screens.signin.LoginScreen
import org.guivicj.support.ui.screens.signin.RegisterScreen
import org.guivicj.support.ui.screens.stats.TechListScreen
import org.guivicj.support.ui.screens.stats.AppStatsScreen
import org.guivicj.support.ui.screens.stats.TechStatsScreen
import org.guivicj.support.ui.screens.tickets.AssignedTicketsScreen
import org.guivicj.support.ui.screens.tickets.TicketDetailScreen
import org.guivicj.support.ui.settings.SettingsScreen
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun HomeNav() {
    val navController = rememberNavController()
    NavHostMain(navController)
}

@Composable
fun NavHostMain(navController: NavHostController) {
    val userViewModel = getKoin().get<UserViewModel>()
    val ticketViewModel = getKoin().get<TicketViewModel>()
    val passwordViewModel = getKoin().get<ForgotPasswordViewModel>()
    val techAnalyticsViewModel = getKoin().get<TechAnalyticsViewModel>()
    val settingsViewModel = getKoin().get<SettingsViewModel>()
    val techViewModel = getKoin().get<TechViewModel>()

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = Screen.FirstOnBoardingScreen.route
        ) {
            composable(Screen.FirstOnBoardingScreen.route) {
                FirstOnBoardingScreen(navController)
            }
            composable(Screen.SecondOnBoardingScreen.route) {
                SecondOnBoardingScreen(navController)
            }
            composable(Screen.ThirdOnBoardingScreen.route) {
                ThirdOnBoardingScreen(navController)
            }
            composable(Screen.FourthOnBoardingScreen.route) {
                FourthOnBoardingScreen(navController)
            }
            composable(Screen.LoginScreen.route) {
                LoginScreen(navController)
            }
            composable(Screen.RegisterScreen.route) {
                RegisterScreen(navController)
            }
            composable(Screen.HomeScreen.route) {
                HomeScreen(navController, userViewModel, ticketViewModel)
            }
            composable(
                route = Screen.ProfileScreen.route,
                arguments = listOf(navArgument("userId") {
                    type = NavType.LongType
                })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getLong("userId") ?: return@composable

                val userByIdState by userViewModel.userById.collectAsState()

                LaunchedEffect(userId) {
                    userViewModel.fetchUserById(userId)
                }

                userByIdState?.let { user ->
                    val currentUser = userViewModel.getCurrentUser()
                    val isEditable = currentUser?.id == user.id
                    ProfileScreen(
                        navController = navController,
                        user = user,
                        isEditable = isEditable,
                        onSaveChanges = { updatedUser ->
                            userViewModel.updateUser(updatedUser)
                        },
                        userViewModel = userViewModel,
                    )
                }
            }
            composable(Screen.TicketDetailScreen.route) { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId") ?: return@composable
                val ticket by ticketViewModel.selectedTicket.collectAsState()

                LaunchedEffect(ticketId) {
                    ticketViewModel.fetchTicketById(ticketId)
                }

                ticket?.let {
                    TicketDetailScreen(
                        ticketDTO = it,
                        navController = navController,
                        ticketViewModel = ticketViewModel,
                        userViewModel = userViewModel,
                        techViewModel = techViewModel
                    )
                }
            }
            composable(Screen.ForgotPasswordScreen.route) {
                ForgotPasswordScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = passwordViewModel
                )
            }
            composable(Screen.AssignedTicketsScreen.route) {
                AssignedTicketsScreen(
                    navController = navController,
                    ticketViewModel = ticketViewModel,
                    userViewModel = userViewModel
                )
            }
            composable(Screen.TechStatsScreen.route) { backStackEntry ->
                val techId = backStackEntry.arguments?.getString("techId")?.toLongOrNull()
                    ?: return@composable
                TechStatsScreen(
                    technicianId = techId,
                    viewModel = techAnalyticsViewModel,
                    navController = navController
                )
            }
            composable(Screen.SettingsScreen.route) {
                val darkTheme by settingsViewModel.darkTheme.collectAsState()
                val language by settingsViewModel.language.collectAsState()
                SettingsScreen(
                    navController = navController,
                    isDarkTheme = darkTheme,
                    onThemeChange = { settingsViewModel.toggleTheme() },
                    userDTO = userViewModel.getCurrentUser() ?: return@composable,
                    currentLanguage = language,
                    onLanguageChange = { settingsViewModel.changeLanguage(it) }
                )
            }
            composable(Screen.AppStatsScreen.route) {
                AppStatsScreen(
                    navController = navController,
                    viewModel = techViewModel
                )
            }
            composable(Screen.TechListScreen.route) {
                TechListScreen(
                    navController = navController,
                    viewModel = techViewModel
                )
            }
        }
    }
}