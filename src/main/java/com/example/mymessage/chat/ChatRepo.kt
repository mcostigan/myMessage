package com.example.mymessage.chat

import com.example.mymessage.chat.model.ChatMetadata
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*


interface ChatRepo : MongoRepository<ChatMetadata, UUID> {

    // TODO: index on userId
    fun getChatsByUsers_Id(user: UUID): Collection<ChatMetadata>

}