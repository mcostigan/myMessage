package com.example.mymessage.chat

import com.example.mymessage.chat.model.Chat
import com.example.mymessage.chat.model.ChatMetadata
import com.example.mymessage.chat.model.NewChat
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = ["http://localhost:4200"])
class ChatController(
    private var chatService: ChatService,
    private val simpleMessagingTemplate: SimpMessageSendingOperations
) {

    @PostMapping
    fun createChat(@RequestBody newChat: NewChat, authentication: Authentication): ChatMetadata {
        val chat = chatService.addChat(newChat.apply { creator = UUID.fromString(authentication.name) })
        chat.users.forEach {
            simpleMessagingTemplate.convertAndSendToUser(it.id.toString(), "/topic/newChat", chat)
        }

        return chat
    }


    @GetMapping()
    fun getMyChat(authentication: Authentication): Collection<Chat> =
        chatService.getUserChats(UUID.fromString(authentication.name))
}