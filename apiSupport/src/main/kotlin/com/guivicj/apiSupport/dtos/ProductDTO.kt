package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.ProductType

data class ProductDTO(
    var name: String,
    var type: ProductType,
    var description: String,
)
