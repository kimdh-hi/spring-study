package com.toy.springxssprotection.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

fun toJson(obj: Any): String {
  val mapper = ObjectMapper()
  return mapper.writeValueAsString(obj)
}

inline fun <reified T> fromJsonToObj(json: String): T {
  val mapper = ObjectMapper()
  return mapper.readValue(json, T::class.java)
}

fun convertToParams(vo: Any): MultiValueMap<String, String> {
  val params: MultiValueMap<String, String> = LinkedMultiValueMap()

  val objectMapper = ObjectMapper()
  objectMapper.registerModule(JavaTimeModule())
  objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  val map: Map<String, String> = objectMapper.convertValue(vo, object : TypeReference<Map<String, String>>() {})

  params.setAll(map)
  return params

}