package com.guivicj.apiSupport.dtos.requests

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val telephone: Int,
    val userTypeCode: String
    )