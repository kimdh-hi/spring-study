package com.toy.springevent.service

import com.toy.springevent.domain.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest(
  private val userService: UserService,
  private val userRepository: UserRepository,
) {

  @Test
  fun signupWithException() {
    userService.signup("test@gmail.com", true)
  }

  @Test
  fun signupWithException2() {
    val username = "test@gmail.com"
    assertThrows<RuntimeException> { userService.signup(username, false, true) }

    val user = userRepository.findByUsername(username)
    assertThat(user).isNull()
  }

  @Test
  fun signup() {
    userService.signup("test@gmail.com")
  }
}
