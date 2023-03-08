package com.toy.springredisevent.user.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.repository.CrudRepository

@RedisHash("UserStatus")
data class UserStatusHash(
  @Id
  val userId: String,

  val statusName: String,

  @TimeToLive
  val ttlSeconds: Int
)

interface UserStatusHashRepository: CrudRepository<UserStatusHash, String>