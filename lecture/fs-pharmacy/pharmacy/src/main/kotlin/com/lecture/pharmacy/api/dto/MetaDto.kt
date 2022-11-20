package com.lecture.pharmacy.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MetaDto(
  @JsonProperty("total_count")
  val totalCount: Int
)