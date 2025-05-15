package org.guivicj.support.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.domain.repository.AuthRepository
import org.guivicj.support.domain.usecase.ValidatePassword
import org.guivicj.support.state.LoginUiState
import org.guivicj.support.validation.ValidationResult

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val validatePassword: ValidatePassword,
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(password = value)
    }

    private fun validate(): Boolean {
        val passwordResult = validatePassword(_state.value.password)
        _state.value = _state.value.copy(
            error = (passwordResult as? ValidationResult.Error)?.message
        )
        return passwordResult is ValidationResult.Success
    }

    fun login() {
        if (!validate()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)

            val result = authRepository.login(_state.value.email, _state.value.password)

            if (result.isSuccess) {
                userViewModel.setUser(result.getOrNull()!!.user)
                _state.value = _state.value.copy(
                    loading = false,
                    session = result.getOrNull(),
                    error = null
                )
            } else {
                val message = result.exceptionOrNull()?.message.orEmpty()
                val error = if ("ERROR" in message) {
                    "Wrong email or password"
                } else {
                    "Login failed"
                }

                _state.value = _state.value.copy(
                    loading = false,
                    error = error
                )
            }
        }
    }

    fun saveFcmToken(fcmToken: String) {
        viewModelScope.launch {
            try {
                val userId = state.value.session?.user?.id ?: return@launch
                println("Saving FCM token for user $userId: $fcmToken")
                authRepository.saveFcmToken(userId, fcmToken)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Failed to save FCM token")
            }
        }
    }

    fun loginWithToken(idToken: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val result = authRepository.loginWithFirebase(idToken)
            _state.value = if (result.isSuccess) {
                userViewModel.setUser(result.getOrNull()!!.user)
                _state.value.copy(
                    loading = false,
                    session = result.getOrNull()
                )
            } else {
                _state.value.copy(loading = false)
            }
        }
    }
}