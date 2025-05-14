package org.guivicj.support.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.domain.repository.AuthRepository
import org.guivicj.support.state.ForgotPasswordUIState

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordUIState())
    val state: StateFlow<ForgotPasswordUIState> = _state

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun sendResetEmail() {
        val emailValue = state.value.email
        if (emailValue.isEmpty()) {
            _state.value = _state.value.copy(message = "Please enter your email")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, message = null)

            val result = authRepository.sendResetEmail(emailValue)

            _state.value = _state.value.copy(
                loading = false,
                message = result.fold(
                    onSuccess = { "Password reset email sent" },
                    onFailure = { "Failed to send password reset email" }
                )
            )
        }
    }
}