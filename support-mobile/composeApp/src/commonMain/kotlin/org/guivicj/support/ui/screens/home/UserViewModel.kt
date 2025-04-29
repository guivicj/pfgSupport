package org.guivicj.support.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.data.model.UserType
import org.guivicj.support.domain.model.UserDTO
import org.guivicj.support.domain.repository.UserRepository

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserUIState())
    val state = _state.asStateFlow()
    private val _userById = MutableStateFlow<UserDTO?>(null)
    val userById = _userById.asStateFlow()

    fun fetchUserById(userId: Long) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                _userById.value = user
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUser(userDTO: UserDTO) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(userDTO)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCurrentUser(): UserDTO {
        return _userById.value!!
    }

    fun setUser(user: UserDTO) {
        _state.value = UserUIState(
            id = user.id!!,
            name = user.name,
            email = user.email,
            telephone = user.telephone.toString(),
            type = user.type
        )
    }

    fun logout() {
        _state.value = UserUIState()
    }

    fun onNameChanged(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun onTelephoneChanged(telephone: String) {
        _state.value = _state.value.copy(telephone = telephone)
    }
}

data class UserUIState(
    val id: Long = 0L,
    val name: String = "",
    val email: String = "",
    val telephone: String = "",
    val type: UserType = UserType.USER,
)
