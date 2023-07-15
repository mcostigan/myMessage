package com.example.mymessage.chat

import com.example.mymessage.chat.model.ChatMetadata
import com.example.mymessage.chat.model.NewChat
import com.example.mymessage.user.UserService
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatConverter(private val userService: UserService) : Converter<NewChat, ChatMetadata> {
    override fun convert(source: NewChat): ChatMetadata {
        val chatMetadata = ChatMetadata()

        chatMetadata.description = source.description
        chatMetadata.creator = userService.getUser(source.creator)
        chatMetadata.createdAt = source.createdAt
        chatMetadata.id = UUID.randomUUID()
        chatMetadata.users = source.users.map { userService.getByName(it) }.plus(chatMetadata.creator).toMutableList()
        chatMetadata.name = source.name

        return chatMetadata
    }

}