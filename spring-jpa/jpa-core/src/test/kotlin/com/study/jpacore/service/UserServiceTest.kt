package com.study.jpacore.service

import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val userService: UserService,
  private val entityManager: EntityManager,
) {

  @Test
  fun save() {
    userService.save("test")
  }

  @Test
  @Transactional
  fun delete() {
    userService.delete("u1")
    entityManager.flush()
  }
}
