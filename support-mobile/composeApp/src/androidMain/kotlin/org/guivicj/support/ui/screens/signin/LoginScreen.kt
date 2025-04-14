package org.guivicj.support.ui.screens.signin

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import org.guivicj.support.R
import org.guivicj.support.firebase.firebaseAuthWithGoogle
import org.guivicj.support.navigation.Screen
import org.guivicj.support.ui.screens.signin.components.FormTextField
import org.guivicj.support.ui.screens.signin.components.PasswordTextField
import org.guivicj.support.ui.screens.signin.components.SignInHeader
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.email_field
import support_mobile.composeapp.generated.resources.forgot_password
import support_mobile.composeapp.generated.resources.login_btn
import support_mobile.composeapp.generated.resources.login_google
import support_mobile.composeapp.generated.resources.login_options
import support_mobile.composeapp.generated.resources.login_subtitle
import support_mobile.composeapp.generated.resources.login_title
import support_mobile.composeapp.generated.resources.password_field

@OptIn(KoinExperimentalAPI::class)
@Composable
actual fun LoginScreen(navController: NavHostController) {
    val viewModel = koinViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val activity = context as Activity

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                firebaseAuthWithGoogle(idToken) { success, token ->
                    if (success && token != null) {
                        viewModel.loginWithToken(token)
                    } else {
                        Toast.makeText(context, "Google login failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun googleSignInOptions(context: Context): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }


    LaunchedEffect(state.session) {
        if (state.session != null) {
            navController.navigate(Screen.FirstOnBoardingScreen.route) {
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
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.login_title),
                subTitle = stringResource(Res.string.login_subtitle)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                FormTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = stringResource(Res.string.email_field)
                )
                PasswordTextField(
                    value = state.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = stringResource(Res.string.password_field)
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    stringResource(Res.string.forgot_password),
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .clickable { navController.navigate(Screen.FirstOnBoardingScreen.route) }
                        .fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            try {
                                viewModel.login()
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
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 10.dp,
                        focusedElevation = 8.dp,
                    )
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = stringResource(Res.string.login_btn)
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                if (state.message.isNotBlank()) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.padding(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = stringResource(Res.string.login_options),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedButton(
                    onClick = {
                        val gso = googleSignInOptions(context)
                        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 10.dp,
                        focusedElevation = 8.dp,
                    )
                ) {
                    Icon(
                        painterResource(
                            id = R.drawable.ic_google,
                        ), contentDescription = null,
                        modifier = Modifier.height(20.dp)
                    )
                    Text(text = stringResource(Res.string.login_google))
                }
            }
        }
    }
}
