package com.toy.springr2dbc.r2dbc

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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
  fun findAll() = runBlocking {
    val users = userR2dbcRepository.findAll().toList()
    println(users)
  }
}