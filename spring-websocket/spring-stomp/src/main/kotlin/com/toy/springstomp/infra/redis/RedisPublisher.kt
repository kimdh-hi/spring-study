package com.toy.springstomp.infra.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class RedisPublisher(
  private val redisTemplate: StringRedisTemplate,
  private val jsonMapper: JsonMapper,
) {
  fun publish(channel: String, message: Any): Long =
    redisTemplate.convertAndSend(channel, jsonMapper.writeValueAsString(message)) ?: 0L
}
