package org.guivicj.support.domain.repository

import org.guivicj.support.domain.model.UserDTO

interface UserRepository {
    suspend fun getUserById(id: Long): UserDTO
}