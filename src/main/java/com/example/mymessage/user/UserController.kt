package com.example.mymessage.user

import com.example.mymessage.user.model.User
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["http://localhost:4200"])
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody user: User) = userService.addUser(user)

    @GetMapping
    fun getAuthenticatedUser(authentication: Authentication) = userService.getUser(UUID.fromString(authentication.name))
}