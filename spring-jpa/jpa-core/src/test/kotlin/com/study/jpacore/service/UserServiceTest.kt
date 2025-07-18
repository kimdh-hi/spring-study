package com.study.jpacore.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val userService: UserService,
) {

  @Test
  fun save() {
    userService.save("test")
  }
}
