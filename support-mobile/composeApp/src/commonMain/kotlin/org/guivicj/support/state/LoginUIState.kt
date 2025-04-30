package org.guivicj.support.state

import org.guivicj.support.domain.model.UserSessionInfoDTO

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val loading: Boolean = false,
    val session: UserSessionInfoDTO? = null,
)
