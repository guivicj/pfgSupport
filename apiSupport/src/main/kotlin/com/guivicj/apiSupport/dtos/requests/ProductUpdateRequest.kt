package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.models.Product

data class ProductUpdateRequest(
    val product : Product,
    val userType: UserType
)
