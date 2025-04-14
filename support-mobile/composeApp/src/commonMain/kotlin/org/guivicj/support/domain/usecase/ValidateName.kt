package org.guivicj.support.domain.usecase

import org.guivicj.support.validation.ValidationResult

class ValidateName() {
    operator fun invoke(name: String): ValidationResult {
        if (name.isNotBlank()) {
            return ValidationResult.Success
        }
        return ValidationResult.Error("The name can't be blank")

    }
}