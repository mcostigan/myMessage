package com.example.mymessage.message

import com.example.mymessage.message.model.Message
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepo: MongoRepository<Message, UUID> {

    // TODO pageable
    fun getMessagesByChatId(chatId: UUID): Collection<Message>
}