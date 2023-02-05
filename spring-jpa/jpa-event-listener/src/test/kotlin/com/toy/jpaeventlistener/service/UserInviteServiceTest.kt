package com.toy.jpaeventlistener.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserInviteServiceTest(
  private val userInviteService: UserInviteService
) {

  @Test
  fun invite() {
    val requestVO = UserInviteRequestVO("test@gmail.com")
    userInviteService.invite(requestVO)
  }
}