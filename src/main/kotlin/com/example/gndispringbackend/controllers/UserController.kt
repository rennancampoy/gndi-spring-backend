package com.example.gndispringbackend.controllers

import com.example.gndispringbackend.models.User
import com.example.gndispringbackend.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
class UserController(val repository: UserRepository) {
    @PostMapping
    fun create(@RequestBody user: User) = ResponseEntity.ok(repository.save(user))

    @GetMapping
    fun get() = ResponseEntity.ok(repository.findAll())
}