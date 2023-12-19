package com.toy.jpadatetime.repository

import com.toy.jpadatetime.domain.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository
) {

  @Test
  fun test() {
    val user = User(name = "kim").also { userRepository.save(it) }
    println(user)
  }
}