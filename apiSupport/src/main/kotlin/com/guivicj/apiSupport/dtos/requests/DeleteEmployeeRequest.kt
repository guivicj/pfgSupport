package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.UserType

data class DeleteEmployeeRequest(
    val id: Long,
    val userType: UserType
)
