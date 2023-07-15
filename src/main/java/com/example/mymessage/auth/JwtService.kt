package com.example.mymessage.auth

import arrow.core.getOrElse
import com.example.mymessage.user.model.User
import io.github.nefilim.kjwt.JWSHMAC256Algorithm
import io.github.nefilim.kjwt.JWT
import io.github.nefilim.kjwt.sign
import io.github.nefilim.kjwt.verifySignature
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@Service
class JwtService {

    @Value("\${security.jwt.duration}")
    private lateinit var duration: Duration


    @Value("\${security.jwt.secret}")
    private lateinit var secret: String

    fun generate(user: User): String {
        val iat = Instant.now()
        val exp = iat.plus(duration)

        val jwt = JWT.hs256 {
            subject(user.id.toString())
            issuer("myMessage")
            claim("name", user.name)
            issuedAt(LocalDateTime.ofInstant(iat, ZoneId.of("UTC")))
            expiresAt(LocalDateTime.ofInstant(exp, ZoneId.of("UTC")))
        }

        return jwt.sign(secret).getOrElse { throw Exception() }.rendered

    }

    fun verifyToken(jwt: String): Boolean {
        return verifySignature<JWSHMAC256Algorithm>(jwt, secret).fold({ false }, { true })
    }

    fun getUserId(jwt: String): UUID {
        return UUID.fromString(
            JWT.decode(jwt).getOrElse { throw UnauthorizedException() }.subject().getOrElse { throw UnauthorizedException() })
    }
}