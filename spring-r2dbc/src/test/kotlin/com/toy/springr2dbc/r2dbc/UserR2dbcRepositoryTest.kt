package com.toy.springr2dbc.r2dbc

import com.toy.springr2dbc.domain.User
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserR2dbcRepositoryTest @Autowired constructor(
  private val userR2dbcRepository: UserR2dbcRepository
) {

  @Test
  fun crTest() = runBlocking {
    val user = User(name = "name")
    val savedUser = userR2dbcRepository.save(user)
    val findUser = userR2dbcRepository.findById(savedUser.id!!)

    Assertions.assertNotNull(findUser)
  }
}