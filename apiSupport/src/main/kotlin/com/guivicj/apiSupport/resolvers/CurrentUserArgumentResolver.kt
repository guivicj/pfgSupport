package com.guivicj.apiSupport.resolvers

import com.guivicj.apiSupport.annotations.CurrentUser
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.services.AuthService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CurrentUserArgumentResolver(
    private val authService: AuthService
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(CurrentUser::class.java) != null &&
                parameter.parameterType == UserSessionInfoDTO::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val authHeader =
            webRequest.getHeader("Authorization") ?: throw RuntimeException("Authorization header is missing")
        val token = authHeader.removePrefix("Bearer ").trim()
        return authService.getUserFromToken(token)
    }
}
