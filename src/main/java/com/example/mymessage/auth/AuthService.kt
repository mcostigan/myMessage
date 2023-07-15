package com.example.mymessage.auth

import com.example.mymessage.user.UserService
import com.example.mymessage.user.model.User
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus

@Service
class AuthService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(authRequest: AuthRequest): AuthResponse {
        try {
            val user = userService.getByName(authRequest.name)
            validatePassword(authRequest.password, user.password)
            return AuthResponse(jwtService.generate(user))
        } catch (e: Exception) {
            throw UnauthorizedException()
        }

    }

    fun register(user: User): AuthResponse {
        userService.addUser(user)
        return AuthResponse(jwtService.generate(user))
    }

    private fun validatePassword(authPassword: String, encodedPassword: String) {
        if (!passwordEncoder.matches(authPassword, encodedPassword)) {
            throw Exception()
        }
    }
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException: Exception("Bad credentials")