package com.toy.springmapper.vo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class JsonNamingTestVO(
  val userId: String
)

data class JsonPropertyTestVO(
  @get:JsonProperty(value = "user_id", access = JsonProperty.Access.WRITE_ONLY)
  var userId: String
)

data class ListTestVO(
  var userIds: List<String> // Mutable, Immutable 상관없음
)