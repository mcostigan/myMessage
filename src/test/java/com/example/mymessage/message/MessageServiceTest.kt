package com.example.mymessage.message

import com.example.mymessage.message.model.Emotion
import com.example.mymessage.message.model.Message
import com.example.mymessage.message.model.Reaction
import org.bson.Document
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@ExtendWith(MockitoExtension::class)
class MessageServiceTest {

    @Mock
    private lateinit var messageRepo: MessageRepo

    @Mock
    private lateinit var mongoTemplate: MongoTemplate

    @InjectMocks
    private lateinit var messageService: MessageService


    @Test
    fun `save returns saved message`() {
        // Mocking data
        val message = Message(UUID.randomUUID(), UUID.randomUUID(), "Hello, world!")

        // Mocking repository behavior
        `when`(messageRepo.save(message)).thenReturn(message)

        // Call service method
        val savedMessage = messageService.save(message)

        // Asserts
        assertEquals(message, savedMessage)
    }

    @Test
    fun getMessageByChat_ReturnsMessagesForChat() {
        // Mocking data
        val chatId = UUID.randomUUID()
        val messages = listOf(
            Message(UUID.randomUUID(), chatId, "Message 1"),
            Message(UUID.randomUUID(), chatId, "Message 2"),
            Message(UUID.randomUUID(), chatId, "Message 3")
        )

        // Mocking repository behavior
        `when`(messageRepo.getMessagesByChatId(chatId)).thenReturn(messages)

        // Call service method
        val retrievedMessages = messageService.getMessageByChat(chatId)

        // Asserts
        assertEquals(messages, retrievedMessages)
    }

    @Test
    fun readMessage_UpdatesMessageWithReadByUser() {
        // Mocking data
        val messageId = UUID.randomUUID()
        val readByUser = UUID.randomUUID()


        // Call service method
        messageService.readMessage(messageId, readByUser)

        // Verify correct update operation is performed
        val queryCaptor = ArgumentCaptor.forClass(Query::class.java)
        val updateCaptor = ArgumentCaptor.forClass(Update::class.java)
        val collectionNameCaptor = ArgumentCaptor.forClass(String::class.java)

        verify(mongoTemplate).updateFirst(
            queryCaptor.capture(),
            updateCaptor.capture(),
            collectionNameCaptor.capture()
        )

        val capturedQuery = queryCaptor.value
        val capturedUpdate = updateCaptor.value
        val capturedCollectionName = collectionNameCaptor.value

        assertEquals(Query.query(Criteria.where("_id").`is`(messageId)), capturedQuery)
        val update = (capturedUpdate.updateObject["\$addToSet"] as Document)
        assertTrue(update.containsKey("readBy"))
        assertEquals(readByUser, update["readBy"])
        assertEquals("message", capturedCollectionName)

    }

    @Test
    fun reactToMessage_UpdatesMessageWithReaction() {
        // Mocking data
        val messageId = UUID.randomUUID()
        val reaction = Reaction(UUID.randomUUID(), Emotion.THUMBS_UP)

        // Call service method
        messageService.reactToMessage(messageId, reaction)

        // Verify correct update operation is performed
        val queryCaptor = ArgumentCaptor.forClass(Query::class.java)
        val updateCaptor = ArgumentCaptor.forClass(Update::class.java)
        val collectionNameCaptor = ArgumentCaptor.forClass(String::class.java)

        verify(mongoTemplate).updateFirst(
            queryCaptor.capture(),
            updateCaptor.capture(),
            collectionNameCaptor.capture()
        )

        val capturedQuery = queryCaptor.value
        val capturedUpdate = updateCaptor.value
        val capturedCollectionName = collectionNameCaptor.value

        assertEquals(Query.query(Criteria.where("_id").`is`(messageId)), capturedQuery)
        val update = (capturedUpdate.updateObject["\$addToSet"] as Document)
        assertTrue(update.containsKey("reactions"))
        assertEquals(reaction, update["reactions"])
        assertEquals("message", capturedCollectionName)
    }
}