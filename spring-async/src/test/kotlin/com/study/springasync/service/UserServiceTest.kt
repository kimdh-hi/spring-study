package com.study.springasync.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val userService: UserService
) {

  @Test
  fun test() {
    assertDoesNotThrow { userService.signup("name") }
  }
}
