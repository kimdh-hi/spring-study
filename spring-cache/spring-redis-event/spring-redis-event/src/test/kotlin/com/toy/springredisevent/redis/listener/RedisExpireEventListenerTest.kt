package com.toy.springredisevent.redis.listener

import com.toy.springredisevent.user.domain.UserStatusHash
import com.toy.springredisevent.user.domain.UserStatusHashRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RedisExpireEventListenerTest @Autowired constructor(
  private val userStatusHashRepository: UserStatusHashRepository
) {

  @Test
  fun test() {
    val userStatusHash = UserStatusHash(userId = "user1", statusName = "status", ttlSeconds = 1)
    userStatusHashRepository.save(userStatusHash)

    Thread.sleep(2000)
  }

}