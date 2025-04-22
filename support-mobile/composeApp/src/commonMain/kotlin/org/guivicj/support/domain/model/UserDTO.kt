package org.guivicj.support.domain.model

import kotlinx.serialization.Serializable
import org.guivicj.support.data.model.UserType


@Serializable
data class UserDTO(
    val id: Long? = null,
    val firebaseUid: String? = null,
    val name: String,
    val email: String,
    val telephone: Int,
    val type: UserType
)
