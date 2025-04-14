package org.guivicj.support.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.domain.model.UserSessionInfoDTO
import org.guivicj.support.domain.repository.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(password = value)
    }

    fun login() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, message = "")
            val result = authRepository.login(_state.value.email, _state.value.password)

            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    loading = false,
                    session = result.getOrNull()
                )
            } else {
                _state.value.copy(
                    loading = false,
                    message = result.exceptionOrNull()?.message ?: "Unknown error"
                )
            }
        }
    }

    fun loginWithToken(idToken: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, message = "")
            val result = authRepository.loginWithFirebase(idToken)

            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    loading = false,
                    session = result.getOrNull()
                )
            } else {
                _state.value.copy(
                    loading = false,
                    message = result.exceptionOrNull()?.message ?: "Unknown error"
                )
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val message: String = "",
    val session: UserSessionInfoDTO? = null
)