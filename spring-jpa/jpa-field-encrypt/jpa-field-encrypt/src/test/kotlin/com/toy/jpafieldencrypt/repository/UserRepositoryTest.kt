package com.toy.jpafieldencrypt.repository

import com.toy.jpafieldencrypt.domain.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import javax.persistence.EntityManager

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserRepositoryTest(
  private val userRepository: UserRepository,
  private val entityManager: EntityManager
) {

  @Test
  fun save() {
    //given
    val user = User(name = "name")

    //when
    val savedUser = userRepository.save(user)
    entityManager.flush()
  }

  @Test
  fun read() {
    //given
    val user = User(name = "name")
    val savedUser = userRepository.save(user)
    entityManager.flush()
    entityManager.clear()

    //when
    val findUser = userRepository.findByIdOrNull(savedUser.id!!)!!

    //then
    println(findUser)
  }

  @Test
  fun readWithCondition() {
    //given
    val user1 = User(name = "name1")
    val user2 = User(name = "name2")
    userRepository.save(user1)
    userRepository.save(user2)
    entityManager.flush()
    entityManager.clear()

    //when
    val result = userRepository.findByName("name1")

    //then
    Assertions.assertNotNull(result)
    Assertions.assertEquals("name1", result?.name)
  }

  @Test
  fun likeSearchFail() {
    //given
    val user1 = User(name = "123user456")
    val user2 = User(name = "56user123")
    val user3 = User(name = "saduser123")
    userRepository.save(user1)
    userRepository.save(user2)
    userRepository.save(user3)
    entityManager.flush()
    entityManager.clear()

    //when
    val result = userRepository.findAllByNameContaining("user")

    //then
    Assertions.assertTrue(result.isNotEmpty())
  }
}