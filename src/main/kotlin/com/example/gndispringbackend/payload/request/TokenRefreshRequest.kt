package com.example.gndispringbackend.payload.request

import javax.validation.constraints.NotBlank

data class TokenRefreshRequest(
    @NotBlank
    val refreshToken: String = ""
)