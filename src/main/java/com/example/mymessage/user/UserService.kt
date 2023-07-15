package com.example.mymessage.user

import com.example.mymessage.user.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepo: UserRepo, private val passwordEncoder: PasswordEncoder, private val imageService: ImageService) {

    fun getUser(userId: UUID): User = userRepo.findById(userId).orElseThrow()

    fun addUser(user: User): User = userRepo.save(user.apply {
        id = UUID.randomUUID()
        password = passwordEncoder.encode(password)
        image = imageService.random()
    }
    )

    fun getByName(name: String): User =
        userRepo.getUserByName(name).orElseThrow()

    fun save(user: User) = userRepo.save(user)
}