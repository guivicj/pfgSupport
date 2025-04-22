package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.UserType

data class UserDTO(
    var id: Long = 0L,
    var name: String = "",
    var email: String = "",
    var telephone: Int = 0,
    var type: UserType = UserType.USER
)
