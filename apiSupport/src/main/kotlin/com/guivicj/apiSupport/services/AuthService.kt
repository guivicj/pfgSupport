package com.guivicj.apiSupport.services

import com.google.firebase.auth.FirebaseAuth
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.mappers.UserMapper
import com.guivicj.apiSupport.repositories.UserRepository
import org.apache.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class AuthService(
    val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {

    fun authenticateWithFirebase(token: String): Response {
        return try {
            val verifiedToken = FirebaseAuth.getInstance().verifyIdToken(token)
            val firebaseUid = verifiedToken.uid
            val email = verifiedToken.email ?: throw Exception("Cannot verify email")

            val user = userRepository.findByFirebaseUid(firebaseUid).orElseGet {
                userMapper.toEntity(userRepository.findByEmail(email).orElseThrow())
            }

            return if (user != null) {
                Response(HttpStatus.SC_OK, "Successfully logged in")
            } else {
                Response(HttpStatus.SC_NOT_FOUND, "User not found / User not registered")
            }
        } catch (e: Exception) {
            Response(HttpStatus.SC_UNAUTHORIZED, e.message ?: "Unauthorized")
        }
    }
}