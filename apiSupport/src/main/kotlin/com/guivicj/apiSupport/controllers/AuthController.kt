package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.dtos.requests.FirebaseLoginRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.mappers.UserMapper
import com.guivicj.apiSupport.repositories.UserRepository
import com.guivicj.apiSupport.services.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/auth")
class AuthController(
    val authService: AuthService
) {

    @GetMapping("/me")
    fun getUserInfo(@RequestHeader("Authorization") authHeader: String): ResponseEntity<UserSessionInfoDTO> {
        val token = authHeader.removePrefix("Bearer ").trim()
        val sessionInfo = authService.getUserFromToken(token)
        return ResponseEntity.ok(sessionInfo)
    }


    @PostMapping("/firebase-login")
    fun loginWithFirebase(@RequestBody request: FirebaseLoginRequest): ResponseEntity<UserSessionInfoDTO> {
        val sessionInfo = authService.authenticateWithFirebase(request)
        return ResponseEntity.ok(sessionInfo)
    }

    @PostMapping("/firebase-logout")
    fun logout(@RequestHeader("Authorization") authHeader: String): ResponseEntity<String> {
        val token = authHeader.removePrefix("Bearer ").trim()
        return authService.logoutWithFirebase(token)
    }
}
