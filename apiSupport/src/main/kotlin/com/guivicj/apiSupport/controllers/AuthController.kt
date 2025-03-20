package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.requests.FirebaseLoginRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.services.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/auth")
class AuthController(val authService: AuthService) {

    @PostMapping("/firebase-login")
    fun loginWithFirebase(@RequestBody request: FirebaseLoginRequest): ResponseEntity<Response> {
        val response = authService.authenticateWithFirebase(request.token)
        return ResponseEntity.status(response.status).body(response)
    }
}