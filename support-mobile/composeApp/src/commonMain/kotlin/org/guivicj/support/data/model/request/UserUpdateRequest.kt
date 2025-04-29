package org.guivicj.support.data.model.request

import kotlinx.serialization.Serializable
import org.guivicj.support.data.model.UserType

@Serializable
data class UserUpdateRequest(
    val name: String?,
    val email: String?,
    val telephone: Int?,
    val userType: UserType?
)
