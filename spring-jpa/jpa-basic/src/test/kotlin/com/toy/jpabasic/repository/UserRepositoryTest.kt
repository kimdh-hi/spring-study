package com.toy.jpabasic.repository

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserRepositoryTest(
  val userRepository: UserRepository
) {

  @Test
  fun test() {
    val user =  userRepository.findById("admin-01")
    println(user)
  }

  @Test
  fun test2() {
    val list = userRepository.findAll()
    list.forEach {
      println(it)
    }
  }

  @Test
  fun `get 성공`() {
    val user = userRepository.get("admin-01")
    assertTrue(user.id == "admin-01")
  }

  @Test
  fun `get 예외`() {
    assertThrows<RuntimeException> {
      userRepository.get("not-exists-user-01")
    }
  }
}