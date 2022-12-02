package com.toy.springcacheex.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serial
import java.io.Serializable

@RedisHash("SpaceCount")
data class SpaceCount(
  @Id
  val spaceId: String,

  val spaceChannelId: String,
  val count: Int
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -2306959680435721706L
  }
}