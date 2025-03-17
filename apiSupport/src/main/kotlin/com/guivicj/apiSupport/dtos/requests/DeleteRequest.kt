package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.UserType

data class DeleteRequest(
    val userType: UserType
)