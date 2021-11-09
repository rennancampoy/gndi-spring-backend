package com.example.gndispringbackend.payload.response

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
