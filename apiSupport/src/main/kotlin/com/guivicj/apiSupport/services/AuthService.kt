package com.guivicj.apiSupport.services

import com.google.firebase.auth.FirebaseAuth
import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.dtos.requests.FirebaseLoginRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.UserMapper
import com.guivicj.apiSupport.models.UserModel
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
            ?: throw RuntimeException("Cannot obtain emission token")

        val issuedAt = Instant.ofEpochSecond(iat)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()


        val user = userRepository.findByFirebaseUid(firebaseUid).orElseThrow()

        return UserSessionInfoDTO(
            user = userMapper.toDTO(user),
            authenticatedSince = issuedAt
        )
    }

    fun authenticateWithFirebase(request: FirebaseLoginRequest): UserSessionInfoDTO {
        val verifiedToken = FirebaseAuth.getInstance().verifyIdToken(request.token)
        val firebaseUid = verifiedToken.uid
        val email = verifiedToken.email ?: throw Exception("Cannot verify email")
        val iat = verifiedToken.claims["iat"] as? Long
            ?: throw RuntimeException("Cannot obtain emission token")
        val issuedAt = Instant.ofEpochSecond(iat)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val user = userRepository.findByFirebaseUid(firebaseUid).orElseGet {
            userRepository.save(
                UserModel(
                    firebaseUid = firebaseUid,
                    email = email,
                    name = request.name ?: "",
                    telephone = request.telephone ?: 0,
                    type = UserType.USER
                )
            )
        }
        if (user.name.isBlank() || user.telephone == 0) {
            user.name = request.name ?: user.name
            user.telephone = request.telephone ?: user.telephone
            userRepository.save(user)
        }
        return UserSessionInfoDTO(
            user = userMapper.toDTO(user),
            authenticatedSince = issuedAt
        )
    }

    fun logoutWithFirebase(token: String): ResponseEntity<String> {
        val verifiedToken = FirebaseAuth.getInstance().verifyIdToken(token)
        val firebaseUid = verifiedToken.uid
        FirebaseAuth.getInstance().revokeRefreshTokens(firebaseUid)
        return ResponseEntity.ok("Successfully logged out")
    }
}
