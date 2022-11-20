package com.lecture.pharmacy.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

class DocumentDto(
  @JsonProperty("address_name")
  val addressName: String,

  @JsonProperty("y")
  val latitude: Double,

  @JsonProperty("x")
  val longitude: Double
)