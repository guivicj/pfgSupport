package com.guivicj.apiSupport.services.validators

import com.guivicj.apiSupport.enums.UserType


class TechnicianValidator : IUserValidator {
    override fun isValid(code: List<String>): Boolean = code[0] == "T" && code[1].length == 8
    override fun getUserType(): UserType = UserType.TECHNICIAN
}