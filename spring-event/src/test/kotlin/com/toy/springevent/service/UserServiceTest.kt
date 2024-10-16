package com.toy.springevent.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest(
  private val userService: UserService,
) {

  @Test
  fun signupWithException() {
    userService.signup("test@gmail.com", true)
  }

  @Test
  fun signup() {
    userService.signup("test@gmail.com")
  }
}
