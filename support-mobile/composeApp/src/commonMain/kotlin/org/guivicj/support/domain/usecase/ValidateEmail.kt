package org.guivicj.support.domain.usecase

import org.guivicj.support.validation.ValidationResult

class ValidateEmail {
    operator fun invoke(email: String): ValidationResult {
        val regex = Regex("^[A-Za-z](.*)(@)(.+)(\\.)(.+)")
        return if (regex.matches(email)) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Please enter a valid email address.")
        }
    }
}