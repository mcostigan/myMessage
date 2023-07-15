package com.example.mymessage.auth

import com.example.mymessage.user.model.User
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class AuthController(private val authService: AuthService) {

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): AuthResponse = authService.login(authRequest)

    @PostMapping("/register")
    fun register(@RequestBody user: User): AuthResponse = authService.register(user)
}