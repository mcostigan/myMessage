package com.example.mymessage.user.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class User(
    @Indexed(unique = true)
    val name: String, var password: String
) {
    @Id
    lateinit var id: UUID
    lateinit var image: String

}