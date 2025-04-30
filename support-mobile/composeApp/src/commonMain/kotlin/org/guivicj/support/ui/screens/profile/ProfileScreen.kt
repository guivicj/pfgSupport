package org.guivicj.support.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.guivicj.support.domain.model.UserDTO
import org.guivicj.support.ui.core.components.GradientTopBackground
import org.guivicj.support.ui.core.components.SubtitleText
import org.guivicj.support.ui.core.components.TitleText
import org.guivicj.support.ui.screens.home.UserViewModel
import org.guivicj.support.ui.screens.home.components.InitialUserProfile
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.logout_btn
import support_mobile.composeapp.generated.resources.phone_field
import support_mobile.composeapp.generated.resources.user_type_field
import support_mobile.composeapp.generated.resources.username_field

@Composable
fun ProfileScreen(
    user: UserDTO,
    isEditable: Boolean,
    onSaveChanges: (UserDTO) -> Unit,
    userViewModel: UserViewModel,
    navController: NavController
) {
    val state by userViewModel.state.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            GradientTopBackground {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    InitialUserProfile(user.name) {}
                    Spacer(modifier = Modifier.height(12.dp))
                    TitleText(user.name)
                    SubtitleText(user.email)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isEditing) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = user.name,
                            onValueChange = { userViewModel.onNameChanged(it) },
                            singleLine = true,
                            label = {
                                Text(
                                    stringResource(Res.string.username_field),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                focusedBorderColor = MaterialTheme.colorScheme.outline,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            ),
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.telephone,
                            onValueChange = { userViewModel.onTelephoneChanged(it) },
                            singleLine = true,
                            label = {
                                Text(
                                    stringResource(Res.string.phone_field),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                focusedBorderColor = MaterialTheme.colorScheme.outline,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            ),
                        )
                    } else {
                        ProfileInfoRow(
                            label = stringResource(Res.string.username_field),
                            value = user.name
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        ProfileInfoRow(
                            label = stringResource(Res.string.phone_field),
                            value = user.telephone.toString()
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    ProfileInfoRow(
                        label = stringResource(Res.string.user_type_field),
                        value = user.type.toString()
                    )
                }
            }
        }

        if (isEditable) {
            OutlinedButton(
                onClick = {
                    userViewModel.logout()
                    navController.navigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.outline,
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp,
                    focusedElevation = 8.dp,
                )
            ) {
                Text(
                    text = stringResource(Res.string.logout_btn),
                    color = MaterialTheme.colorScheme.background
                )
            }

            FloatingActionButton(
                onClick = {
                    if (isEditing) {
                        onSaveChanges(
                            user.copy(
                                name = state.name,
                                telephone = state.telephone.toIntOrNull() ?: 0
                            )
                        )
                    }
                    isEditing = !isEditing
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Filled.Check else Icons.Filled.Edit,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}
