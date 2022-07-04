package com.toy.webfluxr2dbcpostgres.common

import com.fasterxml.jackson.databind.ObjectMapper

object MapperUtils {
  val objectMapper = ObjectMapper()

  fun toJson(obj: Any) = objectMapper.writeValueAsString(obj)
}