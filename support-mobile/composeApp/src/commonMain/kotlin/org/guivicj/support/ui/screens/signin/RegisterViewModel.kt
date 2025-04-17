package org.guivicj.support.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.domain.model.UserSessionInfoDTO
import org.guivicj.support.domain.repository.AuthRepository
import org.guivicj.support.domain.usecase.ValidateEmail
import org.guivicj.support.domain.usecase.ValidateName
import org.guivicj.support.domain.usecase.ValidatePassword
import org.guivicj.support.domain.usecase.ValidatePhone
import org.guivicj.support.validation.ValidationResult

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateName: ValidateName,
    private val validatePhone: ValidatePhone
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(password = value)
    }

    fun onConfirmPasswordChange(value: String) {
        _state.value = _state.value.copy(confirmPassword = value)
    }

    fun onNameChange(value: String) {
        _state.value = _state.value.copy(name = value)
    }

    fun onTelephoneChange(value: String) {
        _state.value = _state.value.copy(telephone = value)
    }

    private fun validate(): Boolean {
        val emailResult = validateEmail(_state.value.email)
        val passwordResult = validatePassword(_state.value.password)
        val nameResult = validateName(_state.value.name)
        val phoneResult = validatePhone(_state.value.telephone)
        if (emailResult is ValidationResult.Error
            || passwordResult is ValidationResult.Error && _state.value.password != _state.value.confirmPassword
            || nameResult is ValidationResult.Error || phoneResult is ValidationResult.Error
        ) {
            _state.value = _state.value.copy(
                emailError = (emailResult as? ValidationResult.Error)?.message,
                passwordError = (passwordResult as? ValidationResult.Error)?.message,
                nameError = (nameResult as? ValidationResult.Error)?.message,
                phoneError = (phoneResult as? ValidationResult.Error)?.message,
            )
            return false
        }
        return true
    }

    fun register() {
        if (validate()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val result = authRepository.register(
                _state.value.name,
                _state.value.email,
                _state.value.password,
                _state.value.telephone.toInt()
            )

            _state.value = if (result.isSuccess) {
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

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val telephone: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val nameError: String? = null,
    val phoneError: String? = null,
    val registered: Boolean = false,
    val loading: Boolean = false,
    val session: UserSessionInfoDTO? = null,
)
