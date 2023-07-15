package com.example.mymessage.chat.model

import com.example.mymessage.user.model.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
open class ChatMetadata {
    @Id
    var id: UUID = UUID.randomUUID()

    open var name: String? = null
    var description: String? = null

    @DBRef
    lateinit var creator: User
    lateinit var createdAt: Instant

    @DBRef

    var users: MutableCollection<User> = mutableListOf()

    fun computeName(forUser: UUID): String {
        return this.name ?: this.users.filter { it.id != forUser }.joinToString(", ") { it.name }
    }
}