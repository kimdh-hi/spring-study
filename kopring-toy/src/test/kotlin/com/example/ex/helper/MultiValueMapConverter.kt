package com.example.ex.helper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.util.LinkedMultiValueMap

object MultiValueMapConverter {

    fun convert(dto: Any) : LinkedMultiValueMap<String, String> {
        val params = LinkedMultiValueMap<String, String>();
        val objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())

        val map = objectMapper.convertValue(dto, object : TypeReference<Map<String, String>>() {})
        params.setAll(map)

        return params
    }


}