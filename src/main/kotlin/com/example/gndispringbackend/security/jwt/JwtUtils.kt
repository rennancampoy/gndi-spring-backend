package com.example.gndispringbackend.security.jwt

import com.example.gndispringbackend.security.services.UserDetailsImpl
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.SignatureException
import java.util.*


@Component
class JwtUtils {
  @Value("\${gndi.app.jwtSecret}")
  private val jwtSecret: String? = null

  @Value("\${gndi.app.jwtExpirationMs}")
  private val jwtExpirationMs = 0

  private val logger = LoggerFactory.getLogger(JwtUtils::class.java)

  fun generateJwtToken(authentication: Authentication): String {
    val userPrincipal = authentication.principal as UserDetailsImpl
    return Jwts.builder()
      .setSubject(userPrincipal.username)
      .setIssuedAt(Date())
      .setExpiration(Date(Date().time + jwtExpirationMs))
      .signWith(SignatureAlgorithm.HS512, jwtSecret)
      .compact()
  }

  fun generateJwtToken(userPrincipal: UserDetailsImpl): String {
    return generateTokenFromUsername(userPrincipal.username)
  }

  fun generateTokenFromUsername(username: String): String {
    return Jwts.builder().setSubject(username).setIssuedAt(Date())
      .setExpiration(Date(Date().time + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
      .compact()
  }

  fun getUserNameFromJwtToken(token: String): String {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
  }

  fun validateJwtToken(authToken: String): Boolean {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
      return true
    } catch (e: SignatureException) {
      logger.error("Invalid JWT signature: {}", e.message)
    } catch (e: MalformedJwtException) {
      logger.error("Invalid JWT token: {}", e.message)
    } catch (e: ExpiredJwtException) {
      logger.error("JWT token is expired: {}", e.message)
    } catch (e: UnsupportedJwtException) {
      logger.error("JWT token is unsupported: {}", e.message)
    } catch (e: IllegalArgumentException) {
      logger.error("JWT claims string is empty: {}", e.message)
    }
    return false
  }
}