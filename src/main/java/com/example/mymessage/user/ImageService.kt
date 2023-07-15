package com.example.mymessage.user

import org.springframework.stereotype.Service
import java.util.*

@Service
class ImageService {

    fun random() = "https://picsum.photos/seed/${Random().nextInt(500)}/200/200"

}