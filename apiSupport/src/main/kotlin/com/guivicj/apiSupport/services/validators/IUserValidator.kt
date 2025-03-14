package com.guivicj.apiSupport.services.validators

import com.guivicj.apiSupport.enums.UserType

interface IUserValidator {
    fun isValid(code: List<String>): Boolean
    fun getUserType(): UserType
}
