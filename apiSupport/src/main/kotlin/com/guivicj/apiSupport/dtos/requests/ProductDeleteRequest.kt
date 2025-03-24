package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.UserType

data class ProductDeleteRequest(
    val productId: Long,
    val userType: UserType
)
