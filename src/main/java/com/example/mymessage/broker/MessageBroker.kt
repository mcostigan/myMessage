package com.example.mymessage.broker

import com.example.mymessage.message.MessageService
import com.example.mymessage.message.model.Emotion
import com.example.mymessage.message.model.Message
import com.example.mymessage.message.model.Reaction
import com.example.mymessage.typing.model.TypingEvent
import com.example.mymessage.typing.model.TypingEventFactory
import com.example.mymessage.user.UserService
import com.example.mymessage.user.model.User
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller
import java.security.Principal
import java.util.*

@Controller
class MessageBroker(
    private var messageService: MessageService,
    private var userService: UserService,
    private val typingEventFactory: TypingEventFactory,
    private val simpleMessagingTemplate: SimpMessageSendingOperations
) {


    @MessageMapping("/chat/{chatId}/message")
    @SendTo("/topic/chat/{chatId}/message")
    fun message(
        @DestinationVariable chatId: UUID,
        message: String,
        principal: Principal,
    ): Message {
        val user = getUser(principal.name)
        val newMessage = Message(chatId, UUID.fromString(principal.name), message)

        // TODO infer from front-end
        simpleMessagingTemplate.convertAndSend(
            "/topic/chat/${chatId}/type",
            typingEventFactory.getTypingEvent(false, user)
        )
        return messageService.save(newMessage)
    }

    @MessageMapping("/chat/{chatId}/type")
    @SendTo("/topic/chat/{chatId}/type")
    fun type(isTyping: Boolean, principal: Principal): TypingEvent {
        return typingEventFactory.getTypingEvent(isTyping, getUser(principal.name))
    }

    private fun getUser(userId: UUID): User {
        return userService.getUser(userId)
    }

    @MessageMapping("/message/{messageId}/read")
    fun readMessage(@DestinationVariable messageId: UUID, principal: Principal ){
        messageService.readMessage(messageId, UUID.fromString(principal.name))
    }

    @MessageMapping("/message/{messageId}/react")
    @SendTo("/topic/message/{messageId}/react")
    fun react(@DestinationVariable messageId: UUID, emotion: String, principal: Principal): Reaction{
        val reaction = Reaction(UUID.fromString(principal.name), Emotion.valueOf(emotion))
        messageService.reactToMessage(messageId, reaction)
        return reaction
    }

    private fun getUser(userId: String) = getUser(UUID.fromString(userId))
}

