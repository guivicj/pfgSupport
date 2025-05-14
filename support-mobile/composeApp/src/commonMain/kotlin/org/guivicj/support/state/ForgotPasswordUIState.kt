package org.guivicj.support.state

data class ForgotPasswordUIState(
    val email: String = "",
    var message: String? = "",
    var loading: Boolean = false
)
