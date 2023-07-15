package com.example.mymessage.chat

import com.example.mymessage.chat.model.ChatMetadata
import com.example.mymessage.chat.model.NewChat
import com.example.mymessage.message.MessageService
import com.example.mymessage.message.model.Message
import com.example.mymessage.user.model.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ChatServiceTest {
    @Mock
    lateinit var chatConverter: ChatConverter

    @Mock
    lateinit var chatRepo: ChatRepo

    @Mock
    lateinit var messageService: MessageService

    @InjectMocks
    lateinit var chatService: ChatService

    private val user = User("user", "").apply { id = UUID.randomUUID(); image = "" }
    private val createdBy = User("creator", "").apply { id = UUID.randomUUID(); image = "" }
    private val newChat = NewChat("a new chat", null, listOf(user.name)).apply { creator = createdBy.id }
    private val chat = ChatMetadata().apply {
        name = "a new chat"
        description = null
        creator = createdBy
        users.add(user)
        users.add(createdBy)
        createdAt = Instant.now()
    }

    @Test
    fun addChat() {
        // should convert the new chat to a chatMetadata object and then save
        `when`(chatConverter.convert(newChat)).thenReturn(chat)
        `when`(chatRepo.save(chat)).thenReturn(chat)

        val result = chatService.addChat(newChat)

        assert(result == chat)
        verify(chatConverter).convert(newChat)
        verify(chatRepo).save(chat)
    }

    @Test
    fun getUserChats() {
        `when`(chatRepo.getChatsByUsers_Id(user.id)).thenReturn(listOf(chat))
        val messages = listOf(
            Message(chat.id, createdBy.id, "first message"),
            Message(chat.id, user.id, "second message")
        )
        `when`(messageService.getMessageByChat(chat.id)).thenReturn(messages)

        val result = chatService.getUserChats(user.id)
        assert(result.size == 1)
        val firstChat = result.first()
        assert(firstChat.id == chat.id)
        assert(firstChat.messages.size == 2)
        assert(firstChat.messages.first().text == "first message")
        assert(firstChat.messages.last().text == "second message")

        verify(messageService).getMessageByChat(chat.id)
        verify(chatRepo).getChatsByUsers_Id(user.id)
    }

    @Test
    fun save() {
        `when`(chatRepo.save(chat)).thenReturn(chat)
        assert(chatService.save(chat) == chat)

        verify(chatRepo).save(chat)
    }
}