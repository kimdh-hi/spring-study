package com.toy.productinfo.domain

import com.toy.productinfo.domain.enum.UserRole
import com.toy.productinfo.domain.enum.UserStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UserTest {

  @Test
  fun `newInstance` () {
    //given
    val user =
      User.newInstance(name = "name", username = "username", password = "password", role = UserRole.USER)

    //expect
    assertAll({
      assertNotNull(user)
      assertEquals(UserStatus.ENABLED, user.status)
    })
  }

}