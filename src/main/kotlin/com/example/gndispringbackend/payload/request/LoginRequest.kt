package com.example.gndispringbackend.payload.request

import javax.validation.constraints.NotBlank

data class LoginRequest(
    @NotBlank
    var username: String,

    @NotBlank
    var password: String
)