package com.example.gndispringbackend.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping("/all")
    fun allAccess(): String {
        return "Test."
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('GNDI') or hasRole('ACCREDITED') or hasRole('PROFESSIONAL')")
    fun generalAccess(): String {
        return "General Test."
    }

    @GetMapping("/professional")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    fun professionalAccess(): String {
        return "Professional Test."
    }

    @GetMapping("/gndi")
    @PreAuthorize("hasRole('GNDI')")
    fun gndiAccess(): String {
        return "GNDI Test."
    }
}