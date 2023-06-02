package com.study.swagger.vo

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

data class RequestVO(
  @Schema(
    description = "Request description",
    example = "Example Data",
    required = true, minLength = 2, maxLength = 10,
  )
  val data: String? = null,

  @Schema(oneOf = [Num::class])
  val num: Num,

  val file: MultipartFile?
)

enum class Num {

  ONE, TWO, THREE
}

data class Request2VO(
  @field:BooleanSchema
  val useSome: String
)

@Schema(description = "노출여부", allowableValues = ["1", "0", "Y", "N", "T", "F", "YES", "NO", "ON", "OFF"])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class BooleanSchema