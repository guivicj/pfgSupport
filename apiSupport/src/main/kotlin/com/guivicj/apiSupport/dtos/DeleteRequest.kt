package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.UserType

data class DeleteRequest(
    val userType: UserType
)