package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.UserType

data class UserDTO(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var telephone: Int = 0,
    var type: UserType = UserType.USER
)