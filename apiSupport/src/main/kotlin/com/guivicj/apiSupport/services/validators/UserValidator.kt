package com.guivicj.apiSupport.services.validators

import com.guivicj.apiSupport.enums.UserType

    class UserValidator : IUserValidator {
        override fun isValid(code: List<String>): Boolean = code[0] == "U" && code[1].length == 6
        override fun getUserType(): UserType = UserType.USER
    }