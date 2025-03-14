package com.guivicj.apiSupport.dtos

data class RegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val telephone: Int,
    val userTypeCode: String
    )