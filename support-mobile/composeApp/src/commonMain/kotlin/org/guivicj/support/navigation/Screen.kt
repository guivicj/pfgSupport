package org.guivicj.support.navigation

sealed class Screen(val route: String) {
    data object FirstOnBoardingScreen : Screen("first_onboarding")
    data object SecondOnBoardingScreen : Screen("second_onboarding")
    data object ThirdOnBoardingScreen : Screen("third_onboarding")
    data object FourthOnBoardingScreen : Screen("fourth_onboarding")
    data object LoginScreen : Screen("login")
    data object RegisterScreen : Screen("register")
    data object HomeScreen : Screen("home")
    data object ForgotPasswordScreen : Screen("forgot_password")
    data object ProfileScreen : Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
    data object TicketDetailScreen : Screen("ticket_detail/{ticketId}") {
        fun createRoute(ticketId: Long) = "ticket_detail/$ticketId"
    }
    data object AssignedTicketsScreen : Screen("your-tickets")
    data object TechStatsScreen : Screen("analytics/{techId}") {
        fun createRoute(techId: Long) = "analytics/$techId"
    }
    data object SettingsScreen : Screen("settings")
}