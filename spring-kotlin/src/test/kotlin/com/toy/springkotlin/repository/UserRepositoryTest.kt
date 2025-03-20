package com.toy.springkotlin.repository

import com.toy.springkotlin.entity.User
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val entityManager: EntityManager,
) {

  @Test
  fun updateBySave() {
    val user = userRepository.save(User("user"))
    entityManager.flush()
    entityManager.clear()

    val findUser = userRepository.findByIdOrNull(user.id)!!
    findUser.update("updatedName")
    userRepository.save(findUser)
    entityManager.flush()
    entityManager.clear()

    val updatedUser = userRepository.findByIdOrNull(user.id)!!
    println(updatedUser)
  }

  @Test
  fun saveFindAndDelete() {
    val user = userRepository.save(User("user"))
    entityManager.flush()
    entityManager.clear()

    val findUser = userRepository.findByIdOrNull(user.id)!!
    userRepository.delete(findUser)
    entityManager.flush()
    entityManager.clear()

    val deletedUser = userRepository.findByIdOrNull(user.id)
    assertThat(deletedUser).isNull()
  }

  @Test
  fun saveAndDelete() {
    val user = userRepository.save(User("user"))
    entityManager.flush()

    userRepository.delete(user)
    entityManager.flush()
    entityManager.clear()

    assertThat(userRepository.findByIdOrNull(user.id)).isNull()
  }
}
