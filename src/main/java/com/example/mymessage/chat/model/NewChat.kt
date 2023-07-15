package com.example.mymessage.chat.model

import java.time.Instant
import java.util.*

class NewChat(val name: String?, val description: String?,  val users: Collection<String>){
    lateinit var creator: UUID
    var createdAt: Instant = Instant.now()
}