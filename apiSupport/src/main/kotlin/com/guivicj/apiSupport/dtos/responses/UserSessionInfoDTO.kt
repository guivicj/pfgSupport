package com.guivicj.apiSupport.dtos.responses

import com.guivicj.apiSupport.dtos.UserDTO
import java.time.LocalDateTime

data class UserSessionInfoDTO(
    val user: UserDTO,
    val authenticatedSince: LocalDateTime
)
