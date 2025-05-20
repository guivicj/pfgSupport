package org.guivicj.support.ui.screens.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.guivicj.support.navigation.Screen
import org.guivicj.support.presentation.RegisterViewModel
import org.guivicj.support.ui.core.components.display.FormTextField
import org.guivicj.support.ui.core.components.display.PasswordTextField
import org.guivicj.support.ui.screens.signin.components.SignInHeader
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.conf_password_field
import support_mobile.composeapp.generated.resources.email_field
import support_mobile.composeapp.generated.resources.password_field
import support_mobile.composeapp.generated.resources.phone_field
import support_mobile.composeapp.generated.resources.register_btn
import support_mobile.composeapp.generated.resources.register_subtitle
import support_mobile.composeapp.generated.resources.register_title
import support_mobile.composeapp.generated.resources.username_field

@OptIn(KoinExperimentalAPI::class)
@Composable
actual fun RegisterScreen(navController: NavHostController) {
    val viewModel = koinViewModel<RegisterViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.session) {
        if (state.session != null) {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(0)
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SignInHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp),
                title = stringResource(Res.string.register_title),
                subTitle = stringResource(Res.string.register_subtitle)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                FormTextField(
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = stringResource(Res.string.username_field),
                    error = state.nameError
                )
                FormTextField(
                    value = state.telephone,
                    onValueChange = { viewModel.onTelephoneChange(it) },
                    label = stringResource(Res.string.phone_field),
                    error = state.phoneError
                )
                FormTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = stringResource(Res.string.email_field),
                    error = state.emailError
                )
                PasswordTextField(
                    value = state.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = stringResource(Res.string.password_field),
                    error = state.passwordError
                )
                PasswordTextField(
                    value = state.confirmPassword,
                    onValueChange = { viewModel.onConfirmPasswordChange(it) },
                    label = stringResource(Res.string.conf_password_field),
                    error = state.passwordError
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            try {
                                viewModel.register()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 10.dp,
                        focusedElevation = 8.dp,
                    )
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = stringResource(Res.string.register_btn)
                    )
                }
            }
        }
    }
}