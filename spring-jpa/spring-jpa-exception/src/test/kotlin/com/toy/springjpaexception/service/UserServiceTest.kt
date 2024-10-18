package com.toy.springjpaexception.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest(
  private val userService: UserService,
) {

  @Test
  fun test() {
    val user = userService.save("test@gmail.com", false)
  }

  @Test
  fun test2() {
    val user = userService.save("test@gmail.com", true)
  }
}
