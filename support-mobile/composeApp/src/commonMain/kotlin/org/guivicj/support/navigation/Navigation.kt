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
import org.guivicj.support.ui.screens.home.HomeScreen
import org.guivicj.support.ui.screens.home.UserViewModel
import org.guivicj.support.ui.screens.home.components.TicketViewModel
import org.guivicj.support.ui.screens.onboarding.FirstOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.FourthOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.SecondOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.ThirdOnBoardingScreen
import org.guivicj.support.ui.screens.profile.ProfileScreen
import org.guivicj.support.ui.screens.signin.LoginScreen
import org.guivicj.support.ui.screens.signin.RegisterScreen
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
        }
    }
}