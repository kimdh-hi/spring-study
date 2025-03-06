package com.toy.springkotlin.base

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import org.springframework.validation.BindingResult

@JsonComponent
class BindingResultSerializer: JsonSerializer<BindingResult>() {


  override fun serialize(value: BindingResult, gen: JsonGenerator, serializers: SerializerProvider) {
    val errors: MutableMap<String, MutableList<String>> = mutableMapOf()

    value.fieldErrors.map { error ->
      error.defaultMessage?.let { errors.getOrPut(error.field, ::mutableListOf).add(it) }
    }

    gen.useDefaultPrettyPrinter()
    gen.writeObjectField("message", errors)
  }
}
