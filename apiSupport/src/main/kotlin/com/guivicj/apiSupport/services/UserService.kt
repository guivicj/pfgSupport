package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.dtos.requests.UserUpdateRequest
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.UserMapper
import com.guivicj.apiSupport.repositories.UserRepository
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository
) {
    fun getUsers(): List<UserDTO> = userRepository.findAll().stream().map(userMapper::toDTO).toList()

    fun getUserById(id: Long): Optional<UserDTO> {
        return userRepository.findById(id).map(userMapper::toDTO)
    }

    fun getUserByName(name: String): Optional<UserDTO> {
        return userRepository.findByName(name).map(userMapper::toDTO)
    }

    fun getUserByEmail(email: String): Optional<UserDTO> {
        return userRepository.findByEmail(email).map(userMapper::toDTO)
    }

    fun updateUser(email: String, updateRequest: UserUpdateRequest): UserDTO {
        val user = getUserByEmail(email).orElseThrow { IllegalArgumentException("User not found") }

        updateRequest.name?.let { user.name = it }
        updateRequest.email?.let { user.email = it }
        updateRequest.telephone?.let { user.telephone = it }
        userRepository.save(userMapper.toEntity(user))

        return user;
    }

    @Transactional
    fun deleteUser(email: String): Response {
        val user = userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("User not found with email: $email") }

        userRepository.delete(user)
        return Response(HttpStatus.SC_OK, "User with email $email deleted successfully")
    }
}
