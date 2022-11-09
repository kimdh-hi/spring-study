package com.toy.okhttp3.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KClass

fun toJson(obj: Any): String {
  val mapper = ObjectMapper()
  return mapper.writeValueAsString(obj)
}

fun <T : Any> fromJsonToObj(json: String, returnType: KClass<T>): T {
  val mapper = ObjectMapper()
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  return mapper.readValue(json, returnType.java)
}