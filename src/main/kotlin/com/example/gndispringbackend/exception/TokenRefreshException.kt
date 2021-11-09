package com.example.gndispringbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class TokenRefreshException(token: String, message: String) :
    RuntimeException(String.format("Failed for [%s]: %s", token, message)) {}