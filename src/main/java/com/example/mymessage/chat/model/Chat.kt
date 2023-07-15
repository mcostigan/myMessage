package com.example.mymessage.chat.model

import com.example.mymessage.message.model.Message
import java.util.*

class Chat(chatMetadata: ChatMetadata, var messages: Collection<Message>, requester: UUID) {
    var id: UUID = chatMetadata.id
    var name: String = chatMetadata.computeName(requester)
    var description = chatMetadata.description
    var creator = chatMetadata.creator
    var createdAt = chatMetadata.createdAt
    var users = chatMetadata.users
}