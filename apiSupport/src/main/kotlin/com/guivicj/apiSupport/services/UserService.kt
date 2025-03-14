package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.dtos.UserUpdateRequest
import com.guivicj.apiSupport.mappers.UserMapper
import com.guivicj.apiSupport.models.UserModel
import com.guivicj.apiSupport.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
) {
    fun getUsers(): List<UserDTO> = userRepository.findAll().stream().map(userMapper::toDTO).toList()

    fun getUserById(id: Long): Optional<UserDTO> {
        return userRepository.findById(id).map(userMapper::toDTO)
    }

    fun getUserByName(name: String): Optional<UserDTO> {
        return userRepository.findByName(name)
    }

    fun getUserByEmail(email: String): Optional<UserDTO> {
        return userRepository.findByEmail(email)
    }

    fun updateUser(email: String, updateRequest: UserUpdateRequest): UserDTO {
        val user = getUserByEmail(email).orElseThrow { IllegalArgumentException("User not found") }

        updateRequest.username?.let { user.name = it }
        updateRequest.email?.let { user.email = it }
        updateRequest.telephone?.let { user.telephone = it }
        userRepository.save(userMapper.toEntity(user))

        return user;
    }

}