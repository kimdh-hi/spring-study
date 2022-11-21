package com.lecture.pharmacy.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoResponseDto(
  @JsonProperty("meta")
  val metaDto: MetaDto,

  @JsonProperty("documents")
  val documents: List<DocumentDto>
)