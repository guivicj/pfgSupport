package org.guivicj.support.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.guivicj.support.navigation.Screen
import org.guivicj.support.ui.screens.onboarding.components.GradientBackgroundBox
import org.guivicj.support.ui.screens.onboarding.components.OnBoardingButton
import org.guivicj.support.ui.screens.onboarding.components.OnBoardingTitle
import org.jetbrains.compose.resources.stringResource
import org.koin.core.annotation.KoinExperimentalAPI
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.first_onboarding_button
import support_mobile.composeapp.generated.resources.first_onboarding_subtitle
import support_mobile.composeapp.generated.resources.third_onboarding_button
import support_mobile.composeapp.generated.resources.third_onboarding_subtitle
import support_mobile.composeapp.generated.resources.third_onboarding_title
import support_mobile.composeapp.generated.resources.welcome_title

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ThirdOnBoardingScreen(navController: NavHostController) {
    MaterialTheme {
        Scaffold {
            GradientBackgroundBox()
            Column(
                Modifier.padding(50.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                OnBoardingTitle(
                    stringResource(Res.string.third_onboarding_title),
                    stringResource(Res.string.third_onboarding_subtitle)
                )
                Spacer(modifier = Modifier.weight(1f))
                OnBoardingButton(
                    navController,
                    Screen.FourthOnBoardingScreen.route,
                    stringResource(Res.string.third_onboarding_button)
                )
            }
        }
    }
}