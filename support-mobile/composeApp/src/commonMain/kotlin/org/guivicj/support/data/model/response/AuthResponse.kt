package org.guivicj.support.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val status: Int,
    val message: String
)

