package com.lecture.snsapp.service

import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.exception.ErrorCode
import com.lecture.snsapp.fixture.UserFixture
import com.lecture.snsapp.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest(
  private val userService: UserService
) {

  @MockkBean
  lateinit var userRepository: UserRepository

  @MockkBean
  lateinit var passwordEncoder: PasswordEncoder

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
    val password = "{noop}password"

    //when
    val userFixture = UserFixture.get(username, password)
    every { userRepository.findByUsername(username) } returns userFixture
    every { userRepository.save(any()) } returns userFixture
    every { passwordEncoder.encode(password) } returns "encoded-password"

    //then
    val throwsException = assertThrows<ApplicationException> {
      userService.join(username, password)
    }
    assertEquals(ErrorCode.DUPLICATED_USER_NAME, throwsException.errorCode)
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
    val throwsException = assertThrows<ApplicationException> {
      userService.login(username, password)
    }
    assertEquals(ErrorCode.LOGIN_FAILED, throwsException.errorCode)
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
    val throwsException =  assertThrows<ApplicationException> {
      userService.login(username, wrongPassword)
    }
    assertEquals(ErrorCode.LOGIN_FAILED, throwsException.errorCode)
  }
}