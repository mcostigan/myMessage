package com.example.mymessage.chat

import com.example.mymessage.chat.model.NewChat
import com.example.mymessage.user.UserService
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
internal class ChatConverterTest {
    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var chatConverter: ChatConverter

    private val newChat: NewChat = NewChat("Chat name", "Chat description", listOf("user-name")).apply {
        creator = UUID.randomUUID()
        createdAt = Instant.now()
    }
    private val creatorUser: User = User("", "").apply { id = newChat.creator }
    private val user: User = User("user-name", "").apply { id = UUID.randomUUID() }


    @Test
    fun convert() {
        `when`(userService.getUser(newChat.creator)).thenReturn(creatorUser)
        `when`(userService.getByName(newChat.users.first())).thenReturn(user)
        val chatMetadata = chatConverter.convert(newChat)
        assertNotNull(chatMetadata.id)
        assertEquals(newChat.name, chatMetadata.name)
        assertEquals(newChat.description, chatMetadata.description)
        assertEquals(creatorUser, chatMetadata.creator)
        assertEquals(newChat.createdAt, chatMetadata.createdAt)
        assertTrue(chatMetadata.users.containsAll(listOf(creatorUser, user)))

        // Verify userService.getUser is called for each user
        newChat.users.forEach {
            verify(userService).getByName(it)
        }
        verify(userService).getUser(newChat.creator)
    }
}