package com.example.mymessage.message

import com.example.mymessage.message.model.Message
import com.example.mymessage.message.model.Reaction
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import java.util.*


@Service
class MessageService(private val messageRepo: MessageRepo, private val mongoTemplate: MongoTemplate){

    fun save(message: Message): Message = messageRepo.save(message)

    fun getMessageByChat(chatId: UUID): Collection<Message> = messageRepo.getMessagesByChatId(chatId)

    fun readMessage(messageId: UUID, readBy: UUID) {
        val update = Update()
        update.addToSet("readBy", readBy)
        val query = Query.query(Criteria.where("_id").`is`(messageId))
        mongoTemplate.updateFirst(query, update, "message")
    }

    fun reactToMessage(messageId: UUID, reaction: Reaction) {
        val update = Update()
        update.addToSet("reactions", reaction)
        val query = Query.query(Criteria.where("_id").`is`(messageId))
        mongoTemplate.updateFirst(query, update, "message")
    }
}