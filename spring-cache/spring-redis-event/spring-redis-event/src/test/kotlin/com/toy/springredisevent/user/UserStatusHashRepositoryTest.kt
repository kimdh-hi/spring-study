package com.toy.springredisevent.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class UserStatusHashRepositoryTest @Autowired constructor(
  private val userStatusHashRepository: UserStatusHashRepository
) {

  @Test
  fun test() {
    val userId = "user1"
    val userStatusHash = UserStatusHash(userId = userId, statusName = "name", ttlSeconds = 10)
    userStatusHashRepository.save(userStatusHash)

    val findUserStatusHash = userStatusHashRepository.findByIdOrNull(userId)

    assertEquals(findUserStatusHash?.statusName, "name")
  }
}