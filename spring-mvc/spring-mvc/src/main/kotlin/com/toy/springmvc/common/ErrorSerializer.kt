package com.toy.springmvc.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.slf4j.LoggerFactory
import org.springframework.boot.jackson.JsonComponent
import org.springframework.validation.BindingResult
import java.io.IOException

@JsonComponent
class BindingResultSerializer : JsonSerializer<BindingResult>() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun serialize(bindingResult: BindingResult, gen: JsonGenerator, sp: SerializerProvider) {

    val errors: MutableMap<String, MutableList<String>> = mutableMapOf()

    bindingResult.fieldErrors.forEach { error ->
      error.defaultMessage?.let { errors.getOrPut(error.field, ::mutableListOf).add(it) }
    }

    log.error("bindingResult errors: {}", errors)

    gen.prettyPrinter = DefaultPrettyPrinter()
    gen.writeStartObject()
    gen.writeStringField("errorCode", ErrorCodes.INVALID_PARAMETER)
    gen.writeObjectField("message", errors)
    gen.writeEndObject()
  }
}