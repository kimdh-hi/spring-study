package com.toy.springcacheex.repository

import com.toy.springcacheex.domain.BlockedToken
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserBlockedTokenRepositoryTest @Autowired constructor(
  private val blockedTokenRepository: BlockedTokenRepository,
) {

  @Test
  fun test() {
    blockedTokenRepository.save(BlockedToken(userId = "user1", accessToken = "token1"))
    blockedTokenRepository.save(BlockedToken(userId = "user1", accessToken = "token2"))
  }

  @Test
  fun findAllByUserId() {
    blockedTokenRepository.findAllByUserId("user1")
      .forEach { println(it) }
  }
}