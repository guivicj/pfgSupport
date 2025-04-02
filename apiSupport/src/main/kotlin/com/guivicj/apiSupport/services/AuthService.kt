package com.guivicj.apiSupport.services

import com.google.firebase.auth.FirebaseAuth
import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.mappers.UserMapper
import com.guivicj.apiSupport.repositories.UserRepository
import org.apache.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId

@Service
class AuthService(
    val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {

    fun getUserFromToken(idToken: String): UserSessionInfoDTO {
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
        val firebaseUid = decodedToken.uid

        val iat = decodedToken.claims["iat"] as? Long
            ?: throw RuntimeException("No se pudo obtener la fecha de emisión")

        val issuedAt = Instant.ofEpochSecond(iat)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()


        val user = userRepository.findByFirebaseUid(firebaseUid).orElseThrow()

        return UserSessionInfoDTO(
            user = userMapper.toDTO(user),
            authenticatedSince = issuedAt
        )
    }

    fun authenticateWithFirebase(token: String): Response {
        return try {
            val verifiedToken = FirebaseAuth.getInstance().verifyIdToken(token)
            val firebaseUid = verifiedToken.uid
            val email = verifiedToken.email ?: throw Exception("Cannot verify email")

            val user = userRepository.findByFirebaseUid(firebaseUid).orElseGet {
                userRepository.findByEmail(email).orElseThrow()
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

    fun logoutWithFirebase(token: String): ResponseEntity<String> {
        val verifiedToken = FirebaseAuth.getInstance().verifyIdToken(token)
        val firebaseUid = verifiedToken.uid
        FirebaseAuth.getInstance().revokeRefreshTokens(firebaseUid)
        return ResponseEntity.ok("Successfully logged out")
    }
}
