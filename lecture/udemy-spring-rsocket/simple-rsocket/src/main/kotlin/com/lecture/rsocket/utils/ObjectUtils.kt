package com.lecture.rsocket.utils

import com.fasterxml.jackson.databind.ObjectMapper
import io.rsocket.Payload
import io.rsocket.util.DefaultPayload

object ObjectUtils {

  fun toPayload(any: Any): Payload {
    val objectMapper = ObjectMapper()
    val bytes = objectMapper.writeValueAsBytes(any)
    return DefaultPayload.create(bytes)
  }

  fun <T> toObject(payload: Payload, clazz: Class<T>): T {
    val objectMapper = ObjectMapper()
    val bytes = payload.data.array()
    return objectMapper.readValue(bytes, clazz)
  }
}