package com.toy.springkotlin.repository

import com.toy.springkotlin.base.extensions.findByIdOrThrow
import com.toy.springkotlin.base.extensions.get
import com.toy.springkotlin.entity.User
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
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

  private val log = LoggerFactory.getLogger(UserRepositoryTest::class.java)

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
    assertThat(updatedUser.name).isEqualTo("updatedName")
    log.debug("updatedUser={}", updatedUser)
  }

  @Test
  fun updateByDirtyCheck() {
    val user = userRepository.save(User("user"))
    entityManager.flush()
    entityManager.clear()

    val findUser = userRepository.findByIdOrNull(user.id)!!
    findUser.update("updatedName")
    entityManager.flush()
    entityManager.clear()

    val updatedUser = userRepository.findByIdOrNull(user.id)!!
    assertThat(updatedUser.name).isEqualTo("updatedName")
    log.debug("updatedUser={}", updatedUser)
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

  @Test
  fun findByIdOrThrow() {
    val user = userRepository.save(User("user"))
    val findUser = userRepository.findByIdOrThrow(user.id)
    println(findUser!!.createdAt)
    assertThat(findUser).isNotNull
  }

  @Test
  fun get() {
    val user = userRepository.save(User("user"))
    val findUser = userRepository[user.id]
    assertThat(findUser).isNotNull
  }

  @Test
  fun findByIdOrThrowNotExists() {
    assertThrows<RuntimeException> {
      userRepository.findByIdOrThrow("notExistsId")
    }
  }

  @Test
  @Disabled
  fun createSleepSave() {
    val userEntity = User("user")
    println(userEntity.createdAt)
    Thread.sleep(2_000)
    val user = userRepository.save(userEntity)
    val findUser = userRepository.findByIdOrThrow(user.id)
    println(findUser!!.createdAt)
    assertThat(findUser).isNotNull
  }
}
