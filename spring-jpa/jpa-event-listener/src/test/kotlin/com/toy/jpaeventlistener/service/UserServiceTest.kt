package com.toy.jpaeventlistener.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest(
  private val userService: UserService
) {

  @Test
  fun save() {
    //given
    val requestVO = UserSaveRequestVO("username@gmail.com")

    //when
    userService.save(requestVO)
  }
}