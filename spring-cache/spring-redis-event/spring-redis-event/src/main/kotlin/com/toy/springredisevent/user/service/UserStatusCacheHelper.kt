package com.toy.springredisevent.user.service

import com.toy.springredisevent.common.CacheConstants
import com.toy.springredisevent.user.domain.UserStatus
import org.springframework.data.redis.cache.CacheKeyPrefix
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class UserStatusCacheHelper(
  private val redisTemplate: RedisTemplate<String, Any>
) {

  val valueOperation: ValueOperations<String, Any> by lazy {
    redisTemplate.opsForValue()
  }

  fun put(userId: String, userStatus: UserStatus,  duration: Duration) {
    val key = generateKey(userId)
    valueOperation.set(key, userStatus, duration)
  }

  private fun generateKey(userId: String): String {
    return "prefix:${CacheConstants.USER_STATUS}${CacheKeyPrefix.SEPARATOR}$userId"
  }
}