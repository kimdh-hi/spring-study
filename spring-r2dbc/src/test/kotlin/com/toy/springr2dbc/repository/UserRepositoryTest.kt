package com.toy.springr2dbc.repository

import com.toy.springr2dbc.domain.User
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository
) {

  @Test
  fun crTest() {
    val user = User(name = "name")
    val savedUser = userRepository.save(user)
    val findUser = userRepository.findByIdOrNull(savedUser.id!!)

    assertNotNull(findUser)
  }
}