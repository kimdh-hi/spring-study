package com.toy.springboot3.repository

import com.toy.springboot3.domain.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserRepositoryImplTest @Autowired constructor(
  private val userRepository: UserRepository
) {

  @Test
  fun customFindById() {
    //given
    val savedUser = userRepository.save(User(name = "test"))

    //when
    val result = userRepository.customFindById(savedUser.id!!)

    //then
    assertNotNull(result)
  }
}