package com.toy.springmvc.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import org.springframework.boot.jackson.JsonObjectSerializer
import org.springframework.validation.BindingResult

@JsonComponent
class BindingResultSerializer: JsonObjectSerializer<BindingResult>() {

  override fun serializeObject(bindingResult: BindingResult, generator: JsonGenerator, provider: SerializerProvider) {
    val errors: MutableMap<String, MutableList<String>> = mutableMapOf()

    bindingResult.fieldErrors.map { error ->
      error.defaultMessage?.let { errors.getOrPut(error.field, ::mutableListOf).add(it) }
    }

    generator.useDefaultPrettyPrinter()
    generator.writeStringField("errorCode", ErrorCodes.INVALID_PARAMETER)
    generator.writeObjectField("message", errors)
  }
}