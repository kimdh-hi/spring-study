package com.toy.springcacheex.common

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.stereotype.Component

@Component
class CustomRedisSerializer: RedisSerializer<Any> {

  override fun serialize(t: Any?): ByteArray {
    return Json.encodeToString(t).toByteArray()
  }

  override fun deserialize(bytes: ByteArray?): Any {
    return Json.decodeFromString(bytes.toString())
  }
}