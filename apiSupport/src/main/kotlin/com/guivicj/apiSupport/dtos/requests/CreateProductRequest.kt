package com.guivicj.apiSupport.dtos.requests

import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.models.Product

data class CreateProductRequest(
    var product: Product,
    var userType: UserType
)
