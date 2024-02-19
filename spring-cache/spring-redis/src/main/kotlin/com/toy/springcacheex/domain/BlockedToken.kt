package com.toy.springcacheex.domain

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(timeToLive = 10)
data class BlockedToken(
  @Id
  var id: String? = null,

  @Indexed
  var userId: String,

  var accessToken: String,
)