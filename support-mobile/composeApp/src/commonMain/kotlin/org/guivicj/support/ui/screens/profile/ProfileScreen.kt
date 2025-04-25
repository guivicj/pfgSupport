package org.guivicj.support.ui.screens.profile

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import org.guivicj.support.domain.model.UserDTO
import org.guivicj.support.ui.screens.home.UserViewModel
import org.guivicj.support.ui.screens.signin.components.GradientTopBackground
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.profile_title

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: UserViewModel,
    userDTO: UserDTO,
    isEditable: Boolean = false,
    onProfileUpdate: (UserDTO) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    MaterialTheme {
        Scaffold(
            topBar = {
                GradientTopBackground {
                    TopAppBar(
                        title = { Text(stringResource(Res.string.profile_title)) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            }
        ) {

        }
    }
}