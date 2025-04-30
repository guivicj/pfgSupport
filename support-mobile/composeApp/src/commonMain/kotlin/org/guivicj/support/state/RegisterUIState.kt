package org.guivicj.support.state

import org.guivicj.support.domain.model.UserSessionInfoDTO

data class RegisterUIState(
    val id: Long = 0,
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