package com.toy.springkotlin.service

import com.toy.springkotlin.repository.UserRepository
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserServiceTest @Autowired constructor(
  private val userService: UserService,
  private val entityManager: EntityManager,
  private val userRepository: UserRepository,
) {

  @Test
  fun save() {
    assertDoesNotThrow {
      userService.save("test")
      entityManager.flush()
    }
  }

  @Test
  fun update() {
    val userId = userService.save("test")
    val updatedName = "updatedName"

    userService.update(userId, updatedName)
    entityManager.flush()
    entityManager.clear()


    val afterUser = userRepository.findByIdOrNull(userId)!!
    assertThat(afterUser.name).isEqualTo(updatedName)
  }

  @Test
  fun delete() {
    val userId = userService.save("test")
    entityManager.flush()
    entityManager.clear()

    userService.delete(userId)
    entityManager.flush()
  }
}
