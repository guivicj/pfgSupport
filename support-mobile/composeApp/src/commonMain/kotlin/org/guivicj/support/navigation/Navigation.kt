package org.guivicj.support.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.guivicj.support.ui.screens.home.HomeScreen
import org.guivicj.support.ui.screens.home.UserViewModel
import org.guivicj.support.ui.screens.onboarding.FirstOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.FourthOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.SecondOnBoardingScreen
import org.guivicj.support.ui.screens.onboarding.ThirdOnBoardingScreen
import org.guivicj.support.ui.screens.signin.LoginScreen
import org.guivicj.support.ui.screens.signin.RegisterScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun HomeNav() {
    val navController = rememberNavController()

    NavHostMain(navController)
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun NavHostMain(navController: NavHostController) {
    val userViewModel = getKoin().get<UserViewModel>()

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
                HomeScreen(navController, userViewModel)
            }
        }
    }
}