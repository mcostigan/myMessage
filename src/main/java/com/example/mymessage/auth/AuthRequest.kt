package com.example.mymessage.auth

data class AuthRequest(val name: String, val password: String)

data class AuthResponse(val token: String)