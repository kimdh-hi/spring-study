package com.toy.jpacustomgenerator.repository

import com.toy.jpacustomgenerator.domain.User2
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class User2RepositoryTest(
  private val user2Repository: User2Repository
) {

  @Test
  fun test() {
    (1..10)
      .map { user2Repository.save(User2(name = "name$it")) }

    user2Repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
      .forEach { println(it) }
  }
}