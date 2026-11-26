package com.toy.springmfa.infra.security.webauthn

import java.time.Duration
import org.springframework.data.redis.core.RedisOperations

class CeremonyOptionsRepository<T : Any>(
  private val redisOperations: RedisOperations<String, Any>,
  private val type: Class<T>,
  private val keyPrefix: String,
  private val ttl: Duration,
) {

  fun save(username: String, options: T) {
    redisOperations.opsForValue().set(key(username), options, ttl)
  }

  fun getAndDelete(username: String): T? =
    redisOperations.opsForValue().getAndDelete(key(username))?.let(type::cast)

  private fun key(username: String) = "$keyPrefix:$username"
}
