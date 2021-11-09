package com.example.gndispringbackend.payload.response

data class JwtResponse (
    val token: String,
    val refreshToken: String,
    val id: Long,
    val username: String,
    val email: String,
    val roles: List<String>
)