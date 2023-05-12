package com.toy.jpacustomgenerator.repository

import com.toy.jpacustomgenerator.domain.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserRepositoryImplTest @Autowired constructor(
  private val userRepository: UserRepository
) {

  @Test
  fun getList1() {
    //given
    (1..20).forEach { n -> userRepository.save(User(name = "name$n")) }

    //when
    val result = userRepository.getList(size = 10)

    //then
    println(result)
    assertTrue(result.isNotEmpty())
  }

  @Test
  fun getList2() {
    //given
    (1..20).forEach { n -> userRepository.save(User(name = "name$n")) }

    //when
    val result1 = userRepository.getList(size = 10)
    val result2 = userRepository.getList(result1.last().id, 10)

    //then
    println(result1)
    println(result2)
    assertAll({
      assertTrue(result1.isNotEmpty())
      assertTrue(result2.isNotEmpty())
    })
  }

}