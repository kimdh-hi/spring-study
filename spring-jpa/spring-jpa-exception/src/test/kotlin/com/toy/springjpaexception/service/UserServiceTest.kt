package com.toy.springjpaexception.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest(
  private val userService: UserService,
) {

  @Test
  fun test() {
    assertDoesNotThrow {
      userService.save("test@gmail.com", false)
    }
  }

  @Test
  fun test2() {
    assertThrows<RuntimeException> { userService.save("test@gmail.com", true) }
  }

  @Test
  fun test3() {
    assertDoesNotThrow {
      userService.save2("test@gmail.com", true)
    }
  }
}
