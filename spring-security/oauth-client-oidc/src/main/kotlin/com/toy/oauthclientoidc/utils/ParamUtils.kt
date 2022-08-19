package com.toy.oauthclientoidc.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

object ParamUtils {
  fun convert(vo: Any): MultiValueMap<String, String> {
    return try {
      val params: MultiValueMap<String, String> = LinkedMultiValueMap()

      val objectMapper = ObjectMapper()
      objectMapper.registerModule(JavaTimeModule())
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

      val map: Map<String, String> = objectMapper.convertValue(vo, object : TypeReference<Map<String, String>>() {})

      params.setAll(map)
      params
    } catch (e: Exception) {
      throw IllegalStateException("Parameter 변환중 오류가 발생했습니다.")
    }
  }
}