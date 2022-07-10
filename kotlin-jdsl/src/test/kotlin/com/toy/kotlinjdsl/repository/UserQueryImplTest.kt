package com.toy.kotlinjdsl.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class UserQueryImplTest(val userRepository: UserRepository) {

  @Test
  fun get() {
    //when
    val user = userRepository.get("user-01")

    //expect
    println(user)
    assertAll({
      assertNotNull(user!!.id)
      assertEquals("user-01", user.id)
    })
  }
}