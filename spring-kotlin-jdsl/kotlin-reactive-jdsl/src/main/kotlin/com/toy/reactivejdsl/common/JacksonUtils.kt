package com.toy.reactivejdsl.common

import com.fasterxml.jackson.databind.ObjectMapper

fun toJson(obj: Any): String {
  val mapper = ObjectMapper()
  return mapper.writeValueAsString(obj)
}