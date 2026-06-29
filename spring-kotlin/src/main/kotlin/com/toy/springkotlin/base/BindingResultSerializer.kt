package com.toy.springkotlin.base

import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer
import org.springframework.boot.jackson.JacksonComponent
import org.springframework.validation.BindingResult

@JacksonComponent
class BindingResultSerializer: ValueSerializer<BindingResult>() {


  override fun serialize(value: BindingResult, gen: JsonGenerator, serializers: SerializationContext) {
    val errors: MutableMap<String, MutableList<String>> = mutableMapOf()

    value.fieldErrors.map { error ->
      error.defaultMessage?.let { errors.getOrPut(error.field, ::mutableListOf).add(it) }
    }

    gen.writePOJOProperty("message", errors)
  }
}
