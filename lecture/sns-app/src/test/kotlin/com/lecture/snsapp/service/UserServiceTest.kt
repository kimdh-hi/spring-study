package com.lecture.snsapp.service

import com.lecture.snsapp.domain.User
import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.fixture.UserFixture
import com.lecture.snsapp.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest(
  private val userService: UserService
) {

  @MockkBean
  lateinit var userRepository: UserRepository

  @MockkBean
  lateinit var passwordEncoder: BCryptPasswordEncoder

  @Test
  fun `join 성공`() {
    //given
    val username = "username"
    val password = "password"

    //when
    val userFixture = UserFixture.get(username, password)
    every { userRepository.findByUsername(username) } returns null
    every { userRepository.save(any()) } returns userFixture
    every { passwordEncoder.encode(password) } returns "encoded-password"

    //then
    assertDoesNotThrow {
      userService.join(username, password)
    }
  }

  @Test
  fun `join 실패 - username 중복`() {
    //given
    val username = "username"
    val password = "password"

    //when
    val userFixture = UserFixture.get(username, password)
    every { userRepository.findByUsername(username) } returns userFixture
    every { userRepository.save(any()) } returns userFixture
    every { passwordEncoder.encode(password) } returns "encoded-password"

    //then
    assertThrows<ApplicationException> {
      userService.join(username, password)
    }
  }

  @Test
  fun `login 성공`() {
    //given
    val username = "username"
    val password = "password"
    val userFixture = UserFixture.get(username, password)

    //when
    every { userRepository.findByUsername(username) } returns userFixture
    every { passwordEncoder.matches(password, any()) } returns true

    //then
    assertDoesNotThrow {
      userService.login(username, password)
    }
  }

  @Test
  fun `login 실패 - 가입된 user가 없는 경우`() {
    //given
    val username = "username"
    val password = "password"

    //when
    every { userRepository.findByUsername(username) } returns null

    //then
    assertThrows<ApplicationException> {
      userService.login(username, password)
    }
  }

  @Test
  fun `login 실패 - password가 틀린 경우`() {
    //given
    val username = "username"
    val password = "password"
    val wrongPassword = "wrong-password"
    val userFixture = UserFixture.get(username, password)

    //when
    every { userRepository.findByUsername(username) } returns userFixture
    every { passwordEncoder.matches(wrongPassword, any()) } returns false

    //then
    assertThrows<ApplicationException> {
      userService.login(username, wrongPassword)
    }
  }
}