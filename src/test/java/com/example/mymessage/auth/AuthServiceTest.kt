package com.example.mymessage.auth

import com.example.mymessage.user.UserService
import com.example.mymessage.user.model.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
internal class AuthServiceTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var passwordEncoded: PasswordEncoder

    @Mock
    lateinit var jwtService: JwtService

    @InjectMocks
    private lateinit var authService: AuthService


    private var authRequest = AuthRequest("user", "pass")
    private var user = User("user", "encoded-pass")

    @Test
    fun `successfully logs in a valid auth request`() {
        `when`(userService.getByName("user")).thenReturn(user)
        `when`(passwordEncoded.matches("pass", "encoded-pass")).thenReturn(true)
        `when`(jwtService.generate(user)).thenReturn("token")

        val result = authService.login(authRequest)
        assert(result.token == "token")

    }

    @Test
    fun `throws an error if the user does not exist`() {
        `when`(userService.getByName("user")).thenThrow(NoSuchElementException())

        assertThrows<UnauthorizedException> {
            authService.login(authRequest)
        }
    }

    @Test
    fun `throws an error if the password is incorrect`() {
        `when`(userService.getByName("user")).thenReturn(user)
        `when`(passwordEncoded.matches("pass", "encoded-pass")).thenReturn(false)

        assertThrows<UnauthorizedException> {
            authService.login(authRequest)
        }
    }

    @Test
    fun register() {
        `when`(userService.addUser(user)).thenReturn(user)
        `when`(jwtService.generate(user)).thenReturn("token")

        val result = authService.register(user)

        assert(result.token == "token")
        verify(userService).addUser(user)
        verify(jwtService).generate(user)
    }
}