package org.guivicj.support.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserSessionInfoDTO(
    val user: UserDTO,
    val authenticatedSince: LocalDateTime
)

