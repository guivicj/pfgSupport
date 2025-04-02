package com.guivicj.apiSupport.config

import com.guivicj.apiSupport.resolvers.CurrentUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val currentUserArgumentResolver: CurrentUserArgumentResolver,
    private val roleRequiredInterceptor: RoleRequiredInterceptor
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(currentUserArgumentResolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(roleRequiredInterceptor)
    }
}
