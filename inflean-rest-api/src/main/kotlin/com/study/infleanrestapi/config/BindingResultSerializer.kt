package com.study.infleanrestapi.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import org.springframework.validation.BindingResult

@JsonComponent
class BindingResultSerializer: JsonSerializer<BindingResult>() {

    override fun serialize(bingdingResult: BindingResult, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartArray()
        bingdingResult.fieldErrors.forEach { error ->
            gen.writeStartObject()
            gen.writeStringField("field", error.field)
            gen.writeStringField("objectName", error.objectName)
            gen.writeStringField("message", error.defaultMessage)
            gen.writeStringField("code", error.code)
            error.rejectedValue?.let { gen.writeStringField("rejectedValue", it.toString()) }
            gen.writeEndObject()
        }
    }
}