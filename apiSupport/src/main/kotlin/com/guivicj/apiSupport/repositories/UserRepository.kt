package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.models.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserModel, Long> {
    fun findByName(name: String): Optional<UserDTO>
    fun findByEmail(email: String): Optional<UserDTO>
    fun findByFirebaseUid(firebaseUid: String): Optional<UserModel>
    fun existsByEmail(email: String): Boolean
}
