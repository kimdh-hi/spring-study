package com.toy.jpabulkinsert.repository

import com.toy.jpabulkinsert.entity.User
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Transactional
@SpringBootTest
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository
) {

  @Test
  fun saveAll() {
    val users = (1 .. 1000).map { User(name = "name$it") }
//    val users = (1 .. 1000).map { User(id = "id", name = "name$it") }
    userRepository.saveAllAndFlush(users)

    val findUsers = userRepository.findAll()
    println(findUsers.size)
  }
}