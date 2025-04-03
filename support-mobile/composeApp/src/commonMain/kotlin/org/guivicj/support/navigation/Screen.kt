package org.guivicj.support.navigation

sealed class Screen(val route: String) {
    data object FirstOnBoardingScreen : Screen("first_onboarding")
    data object SecondOnBoardingScreen : Screen("second_onboarding")
    data object ThirdOnBoardingScreen : Screen("third_onboarding")
    data object FourthOnBoardingScreen : Screen("fourth_onboarding")
}