package com.example.mymessage.message.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import java.time.Instant
import java.util.*

@Document
class Message(@Indexed(unique = false) var chatId: UUID, var authorId: UUID, var text: String) {
    var readBy: MutableSet<UUID> = mutableSetOf(authorId)

    @Id
    var id: UUID = UUID.randomUUID()

    var timeStamp: Instant = Instant.now()

    var reactions: Collection<Reaction> = listOf()
}