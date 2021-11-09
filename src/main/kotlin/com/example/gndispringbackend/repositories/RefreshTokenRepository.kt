package com.example.gndispringbackend.repositories

import com.example.gndispringbackend.models.RefreshToken
import com.example.gndispringbackend.models.User

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByUser(user: User): Int
}