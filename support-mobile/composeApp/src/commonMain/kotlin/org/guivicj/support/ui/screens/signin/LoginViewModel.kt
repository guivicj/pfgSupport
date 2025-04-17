package org.guivicj.support.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.domain.model.UserSessionInfoDTO
import org.guivicj.support.domain.repository.AuthRepository
import org.guivicj.support.domain.usecase.ValidateEmail
import org.guivicj.support.domain.usecase.ValidatePassword
import org.guivicj.support.ui.screens.home.UserViewModel
import org.guivicj.support.validation.ValidationResult

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail,
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

    private fun validate() {
        val emailResult = validateEmail(_state.value.email)
        val passwordResult = validatePassword(_state.value.password)
        if (emailResult is ValidationResult.Error || passwordResult is ValidationResult.Error) {
            _state.value = _state.value.copy(
                emailError = (emailResult as? ValidationResult.Error)?.message,
                passwordError = (passwordResult as? ValidationResult.Error)?.message,
            )
            return
        }
    }

    fun login() {
        validate()
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val result = authRepository.login(_state.value.email, _state.value.password)
            _state.value = if (result.isSuccess) {
                userViewModel.setUser(result.getOrNull()!!.user)
                _state.value.copy(
                    loading = false,
                    session = result.getOrNull()
                )
            } else {
                _state.value.copy(
                    loading = false,
                )
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
                _state.value.copy(
                    loading = false,
                )
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loading: Boolean = false,
    val session: UserSessionInfoDTO? = null,
)