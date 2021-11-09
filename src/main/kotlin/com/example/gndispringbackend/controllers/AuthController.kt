package com.example.gndispringbackend.controllers

import com.example.gndispringbackend.exception.TokenRefreshException
import com.example.gndispringbackend.models.ERole
import com.example.gndispringbackend.models.RefreshToken
import com.example.gndispringbackend.models.Role
import com.example.gndispringbackend.models.User
import com.example.gndispringbackend.payload.request.LoginRequest
import com.example.gndispringbackend.payload.request.SignupRequest
import com.example.gndispringbackend.payload.request.TokenRefreshRequest
import com.example.gndispringbackend.payload.response.JwtResponse
import com.example.gndispringbackend.payload.response.MessageResponse
import com.example.gndispringbackend.payload.response.RefreshTokenResponse
import com.example.gndispringbackend.repositories.RoleRepository
import com.example.gndispringbackend.repositories.UserRepository
import com.example.gndispringbackend.security.jwt.JwtUtils
import com.example.gndispringbackend.security.services.RefreshTokenService
import com.example.gndispringbackend.security.services.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/auth")
class AuthController(
	private val authenticationManager: AuthenticationManager,
	private val userRepository: UserRepository,
	private val roleRepository: RoleRepository,
	private val refreshTokenService: RefreshTokenService,
	private val encoder: PasswordEncoder,
	private val jwtUtils: JwtUtils
) {
	@PostMapping("/signin")
	fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest): ResponseEntity<*> {
		val authentication: Authentication = authenticationManager
			.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
		SecurityContextHolder.getContext().authentication = authentication
		val userDetails = authentication.principal as UserDetailsImpl
		val jwt = jwtUtils.generateJwtToken(userDetails)
		val roles = userDetails.authorities.stream().map { item: GrantedAuthority -> item.authority }
			.collect(Collectors.toList())
		val refreshToken: RefreshToken = refreshTokenService.createRefreshToken(userDetails.id)
		return ResponseEntity.ok(
			JwtResponse(
				jwt, refreshToken.token, userDetails.id,
				userDetails.username, userDetails.email, roles
			)
		)
	}

	@PostMapping("/refreshToken")
	fun refreshToken(@RequestBody request: @Valid TokenRefreshRequest): ResponseEntity<*> {
		val requestRefreshToken: String = request.refreshToken
		return refreshTokenService.findByToken(requestRefreshToken)
			.map(refreshTokenService::verifyExpiration)
			.map(RefreshToken::user)
			.map { user ->
				val token = jwtUtils.generateTokenFromUsername(user.username)
				ResponseEntity.ok(RefreshTokenResponse(token, requestRefreshToken))
			}
			.orElseThrow { TokenRefreshException(requestRefreshToken, "O refresh token não é válido.") }
	}

	@PostMapping("/signup")
	fun registerUser(@RequestBody signUpRequest: @Valid SignupRequest): ResponseEntity<*> {
		if (userRepository.existsByUsername(signUpRequest.username)) {
			return ResponseEntity
				.badRequest()
				.body(MessageResponse("Username já foi escolhido."))
		}
		if (userRepository.existsByEmail(signUpRequest.email)) {
			return ResponseEntity
				.badRequest()
				.body(MessageResponse("Email já foi escolhido"))
		}

		val user = User(
			username = signUpRequest.username,
			email = signUpRequest.email,
			password = encoder.encode(signUpRequest.password)
		)
		val strRoles: Set<String> = signUpRequest.role
		val roles: MutableSet<Role> = HashSet()
		strRoles.forEach(Consumer { role: String? ->
			when (role) {
				"professional" -> {
					val professionalRole: Role = roleRepository.findByName(ERole.ROLE_PROFESSIONAL)
						.orElseThrow {
							RuntimeException(
								"Error: Role is not found."
							)
						}
					roles.add(professionalRole)
				}
				"accredited" -> {
					val accreditedRole: Role = roleRepository.findByName(ERole.ROLE_ACCREDITED)
						.orElseThrow {
							RuntimeException(
								"Error: Role is not found."
							)
						}
					roles.add(accreditedRole)
				}
				else -> {
					val gndiRole: Role = roleRepository.findByName(ERole.ROLE_GNDI)
						.orElseThrow {
							RuntimeException(
								"Error: Role is not found."
							)
						}
					roles.add(gndiRole)
				}
			}
		})
		user.roles = roles
		userRepository.save(user)
		return ResponseEntity.ok(MessageResponse("Usuário registrado com sucesso."))
	}
}