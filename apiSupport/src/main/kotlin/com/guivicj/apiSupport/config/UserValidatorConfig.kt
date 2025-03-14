package com.guivicj.apiSupport.config

import com.guivicj.apiSupport.services.validators.AdminValidator
import com.guivicj.apiSupport.services.validators.IUserValidator
import com.guivicj.apiSupport.services.validators.TechnicianValidator
import com.guivicj.apiSupport.services.validators.UserValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserValidatorConfig {
    class UserValidatorConfig {
        @Bean
        fun userValidators(): List<IUserValidator> {
            return listOf(AdminValidator(), TechnicianValidator(), UserValidator())
        }
    }
}