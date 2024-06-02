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
    val users = (1 .. 50).map { User(name = "name$it") }
    userRepository.saveAll(users)
    userRepository.flush()
  }
}