package com.toy.jpabasic.repository

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class UserRepositoryImplTest(
  val userRepository: UserRepository
) {

  @Test
  fun test() {
    val list = userRepository.searchList()

    list.forEach {
      println(it)
    }
  }

  @Test
  fun testV2() {
    val listV2 = userRepository.searchListV2()

    listV2.forEach {
      println(it)
    }
  }
}