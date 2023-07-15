package com.example.mymessage.chat

import com.example.mymessage.chat.model.Chat
import com.example.mymessage.chat.model.ChatMetadata
import com.example.mymessage.chat.model.NewChat
import com.example.mymessage.message.MessageService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatService(
    private var chatConverter: ChatConverter,
    private var messageService: MessageService,
    private var chatRepo: ChatRepo
) {

    fun addChat(newChat: NewChat): ChatMetadata {
        val chat = chatConverter.convert(newChat)
        return save(chat)
    }

    fun getUserChats(userId: UUID): Collection<Chat> =
        chatRepo.getChatsByUsers_Id(userId).map { Chat(it, messageService.getMessageByChat(it.id), userId) }

    fun save(chatMetadata: ChatMetadata): ChatMetadata {
        return chatRepo.save(chatMetadata)
    }
}