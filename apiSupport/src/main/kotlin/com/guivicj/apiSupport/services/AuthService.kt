package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.requests.LoginRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.requests.RegisterRequest
import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.UserMapper
import com.guivicj.apiSupport.models.UserModel
import com.guivicj.apiSupport.repositories.UserRepository
import com.guivicj.apiSupport.services.validators.AdminValidator
import com.guivicj.apiSupport.services.validators.TechnicianValidator
import com.guivicj.apiSupport.services.validators.UserValidator
import org.springframework.stereotype.Service

@Service
class AuthService(
    val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {

    fun register(request: RegisterRequest): UserModel {
        if (userRepository.existsByEmail(request.email)) throw Exception("Email already exists")
        val user = UserDTO(
            request.username,
            request.email,
            request.password,
            request.telephone,
            manageType(request.userTypeCode)
        )

        return userRepository.save(userMapper.toEntity(user))
    }

    fun login(request: LoginRequest): Response {
        val user = userRepository.findByEmail(request.email)

        return if (user.isPresent && user.get().password == request.password) {
            Response(200, "Login successful")
        } else {
            Response(401, "Error: Email or password invalid")
        }
    }

    private fun manageType(userTypeCode: String): UserType {
        val code = userTypeCode.split("#")

        val validators = listOf(
            AdminValidator(),
            TechnicianValidator(),
            UserValidator()
        )
        return validators.firstOrNull { it.isValid(code) }?.getUserType() ?: throw Exception("User type not valid")
    }
}