package org.guivicj.support.state

import org.guivicj.support.data.model.UserType

data class UserUIState(
    val id: Long = 0L,
    val name: String = "",
    val email: String = "",
    val telephone: String = "",
    val type: UserType = UserType.USER,
)
