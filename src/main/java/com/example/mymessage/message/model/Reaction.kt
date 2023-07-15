package com.example.mymessage.message.model

import java.util.*

class Reaction(var userId: UUID, var emotion: Emotion)

enum class Emotion {
    LAUGHING,
    THUMBS_UP,
    THUMBS_DOWN,
    EXCLAMATION,
    QUESTION,
    HEART
}
