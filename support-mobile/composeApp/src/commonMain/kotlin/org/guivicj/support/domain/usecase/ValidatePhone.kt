package org.guivicj.support.domain.usecase

import org.guivicj.support.validation.ValidationResult

class ValidatePhone() {
    operator fun invoke(phone: String): ValidationResult {
        if (phone.length == 9 && phone.all { it.isDigit() }) {
            return ValidationResult.Success
        }
        return ValidationResult.Error("Please enter a valid phone number")
    }
}