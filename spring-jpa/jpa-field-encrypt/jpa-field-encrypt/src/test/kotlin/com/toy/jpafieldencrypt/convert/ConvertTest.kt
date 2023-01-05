package com.toy.jpafieldencrypt.convert

import com.toy.jpafieldencrypt.domain.User
import com.toy.jpafieldencrypt.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class ConvertTest(
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
    assertNotNull(result)
    assertEquals("name1", result?.name)
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
    assertTrue(result.isNotEmpty())
  }
}