package org.guivicj.support.domain.usecase

import org.guivicj.support.validation.ValidationResult

class ValidatePassword {
    operator fun invoke(password: String): ValidationResult {
        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return if (regex.matches(password)) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Password must contain at least 8 characters, including uppercase, lowercase, number, and symbol.")
        }
    }
}