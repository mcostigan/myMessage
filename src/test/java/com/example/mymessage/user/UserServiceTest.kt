package com.example.mymessage.user

import com.example.mymessage.user.model.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
internal class UserServiceTest {
    @Mock
    private lateinit var mockImageService: ImageService

    @Mock
    private lateinit var mockUserRepo: UserRepo

    @Mock
    private lateinit var mockPasswordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var userService: UserService

    private val userId = UUID.randomUUID()
    private val user = User("user", "password").apply {
        this.id = userId
        image = "random-image"
        password = "encoded-password"
    }

    @Test
    fun `getUser throws error if user id does not exist`() {
        `when`(mockUserRepo.findById(eq(userId))).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            userService.getUser(userId)
        }

    }

    @Test
    fun getUser() {

        `when`(mockUserRepo.findById(userId)).thenReturn(Optional.of(user))
        assert(userService.getUser(userId) == user)
    }

    @Test
    fun addUser() {
        val user = User("user", "password")
        `when`(mockPasswordEncoder.encode(any())).thenReturn("encoded-password")
        `when`(mockUserRepo.save(eq(user))).thenReturn(user)
        `when`(mockImageService.random()).thenReturn("imageUrl")
        userService.addUser(user)

        verify(mockPasswordEncoder).encode(eq("password"))
        verify(mockImageService).random()
        verify(mockUserRepo).save(eq(user))
        assertNotNull(user.id)
        assert(user.password == "encoded-password")
        assert(user.image == "imageUrl")
    }

    @Test
    fun getByName() {
        `when`(mockUserRepo.getUserByName("user")).thenReturn(Optional.of(user))
        assert(userService.getByName("user") == user)
    }

    @Test
    fun save() {
        `when`(mockUserRepo.save(user)).thenReturn(user)
        assert(userService.save(user) == user)
        verify(mockUserRepo).save(eq(user))
    }
}