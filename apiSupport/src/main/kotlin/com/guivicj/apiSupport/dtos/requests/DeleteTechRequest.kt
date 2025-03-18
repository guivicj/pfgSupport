package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.UserType

data class DeleteTechRequest(
    val id: Long,
    val userType: UserType
)
