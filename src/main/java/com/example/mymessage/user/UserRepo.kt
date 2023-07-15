package com.example.mymessage.user

import com.example.mymessage.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepo : MongoRepository<User, UUID> {
    fun getUserByName(name: String): Optional<User>
}