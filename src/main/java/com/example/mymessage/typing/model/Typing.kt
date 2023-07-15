package com.example.mymessage.typing.model

import com.example.mymessage.user.model.User
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant


abstract class TypingEvent(val user: User) {
    val timeStamp: Instant = Instant.now()

}

@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type")
class IsTypingEvent(from: User, val timeoutSeconds: Int) : TypingEvent(from)

@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "type")
class NotTypingEvent(from: User) : TypingEvent(from)


@Service
class TypingEventFactory {
    @Value("\${typing.timeout}")
    private var timeout: Int = 60

    fun getTypingEvent(isTyping: Boolean, user: User): TypingEvent {
        if (isTyping) {
            return IsTypingEvent(user, timeout)
        } else {
            return NotTypingEvent(user)
        }
    }
}