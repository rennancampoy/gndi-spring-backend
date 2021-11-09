package com.example.gndispringbackend.security.services

import com.example.gndispringbackend.exception.TokenRefreshException
import com.example.gndispringbackend.models.RefreshToken
import com.example.gndispringbackend.repositories.RefreshTokenRepository
import com.example.gndispringbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import javax.transaction.Transactional

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository
) {
    @Value("\${gndi.app.jwtRefreshExpirationMs}")
    private val refreshTokenDurationMs: Long? = null

    fun findByToken(token: String): Optional<RefreshToken> {
        return refreshTokenRepository.findByToken(token)
    }

    fun createRefreshToken(userId: Long): RefreshToken {
        return refreshTokenRepository.save(
            RefreshToken(
                user = userRepository.findById(userId).get(),
                expiryDate = Instant.now().plusMillis(refreshTokenDurationMs!!),
                token = UUID.randomUUID().toString()
            )
        )
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expiryDate < Instant.now()) {
            refreshTokenRepository.delete(token)
            throw TokenRefreshException(token.token, "O refresh token está expirado. Por favor, faça uma nova requisição de signin.")
        }
        return token
    }

    @Transactional
    fun deleteByUserId(userId: Long): Int {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get())
    }
}
