package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.UserType

data class UserUpdateRequest(
    val name: String?,
    val email: String?,
    val telephone: Int?,
    val userType: UserType?
)
