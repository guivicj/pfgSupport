package com.guivicj.apiSupport.config

import com.google.firebase.auth.FirebaseAuth
import com.guivicj.apiSupport.annotations.RoleRequired
import com.guivicj.apiSupport.services.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RoleRequiredInterceptor(
    private val authService: AuthService
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) return true

        val method = handler.method
        val roleRequired = method.getAnnotation(RoleRequired::class.java) ?: return true

        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")?.trim()
            ?: throw RuntimeException("Invalid token")

        val user = authService.getUserFromToken(token)

        if (user.user.type != roleRequired.value) {
            throw RuntimeException("Access denied: required role is ${roleRequired.value}, but you are ${user.user.type}")
        }
        return true
    }
}

