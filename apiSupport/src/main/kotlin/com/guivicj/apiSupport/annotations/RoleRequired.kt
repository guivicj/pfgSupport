package com.guivicj.apiSupport.annotations

import com.guivicj.apiSupport.enums.UserType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RoleRequired(val value: UserType)

