package com.toy.jpacustomgenerator.repository

import com.toy.jpacustomgenerator.domain.User
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class UserRepositoryTest(
  private val userRepository: UserRepository,
  private val entityManager: EntityManager
) {

  @Test
  fun test() {
    (1..5)
      .map {
        userRepository.save(User(name = "name$it"))
        entityManager.flush()
        Thread.sleep(500)
      }

    userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
      .forEach { println(it) }
  }
}