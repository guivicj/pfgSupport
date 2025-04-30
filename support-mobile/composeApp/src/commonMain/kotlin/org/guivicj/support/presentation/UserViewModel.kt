package org.guivicj.support.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.domain.model.UserDTO
import org.guivicj.support.domain.repository.UserRepository
import org.guivicj.support.state.UserUIState

class UserViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(UserUIState())
    val state = _state.asStateFlow()
    private val _userById = MutableStateFlow<UserDTO?>(null)
    val userById = _userById.asStateFlow()
    private var currentUser: UserDTO? = null

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

    fun setCurrentUser(user: UserDTO) {
        currentUser = user
    }

    fun getCurrentUser(): UserDTO? = currentUser

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
        currentUser = null
    }

    fun onNameChanged(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun onTelephoneChanged(telephone: String) {
        _state.value = _state.value.copy(telephone = telephone)
    }
}
