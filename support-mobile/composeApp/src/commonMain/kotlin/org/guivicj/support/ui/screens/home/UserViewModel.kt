package org.guivicj.support.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.guivicj.support.data.model.UserType
import org.guivicj.support.domain.model.UserDTO

class UserViewModel : ViewModel() {
    private val _state = MutableStateFlow(UserUIState())
    val state = _state.asStateFlow()

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
}

data class UserUIState(
    val id: Long = 0L,
    val name: String = "",
    val email: String = "",
    val telephone: String = "",
    val type: UserType = UserType.USER,
)
