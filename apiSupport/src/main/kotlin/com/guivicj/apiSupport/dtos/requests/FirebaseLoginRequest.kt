package com.guivicj.apiSupport.dtos.requests

data class FirebaseLoginRequest(
    val token: String,
    val name: String,
    val telephone: Int
)
