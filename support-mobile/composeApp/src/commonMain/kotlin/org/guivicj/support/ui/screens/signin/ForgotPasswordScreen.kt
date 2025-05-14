package org.guivicj.support.ui.screens.signin

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.guivicj.support.presentation.ForgotPasswordViewModel
import org.guivicj.support.ui.core.components.display.FormTextField
import org.guivicj.support.ui.screens.signin.components.SignInHeader
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.cancel_btn
import support_mobile.composeapp.generated.resources.email_field
import support_mobile.composeapp.generated.resources.forgot_password_subtitle
import support_mobile.composeapp.generated.resources.forgot_password_title
import support_mobile.composeapp.generated.resources.send_email_btn

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    viewModel: ForgotPasswordViewModel
) {
    val state by viewModel.state.collectAsState()

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SignInHeader(
                title = stringResource(Res.string.forgot_password_title),
                subTitle = stringResource(Res.string.forgot_password_subtitle),
                modifier = Modifier.padding(24.dp)
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
                Text(
                    text = state.message ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedButton(
                    onClick = { viewModel.sendResetEmail() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(8.dp),
                            clip = false,
                            spotColor = MaterialTheme.colorScheme.outline
                        ),
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
                        text = stringResource(Res.string.send_email_btn)
                    )
                }
                Spacer(modifier = Modifier.padding(16.dp))
                OutlinedButton(
                    onClick = {
                        onBack()
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
                    Text(text = stringResource(Res.string.cancel_btn))
                }

            }
        }
    }
}