package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.DeleteRequest
import com.guivicj.apiSupport.dtos.DeleteResponse
import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.dtos.UserUpdateRequest
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.UserMapper
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

    fun deleteUser(email: String, deleteRequest: DeleteRequest): DeleteResponse {
        val user = getUserByEmail(email).orElseThrow { IllegalArgumentException("User not found") }
        var isDeleted = false
        if ((deleteRequest.userType).equals(UserType.ADMIN)) {
            userRepository.delete(userMapper.toEntity(user))
            isDeleted = true
        }
        return if (isDeleted) {
            DeleteResponse(200, "Successfully deleted the user")
        } else {
            DeleteResponse(403, "Only Admin is allowed to delete users")
        }
    }
}