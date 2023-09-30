package com.toy.springcacheex.domain

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("SpaceCount")
class VisitorCount(
  @Id
  val spaceId: String,


)