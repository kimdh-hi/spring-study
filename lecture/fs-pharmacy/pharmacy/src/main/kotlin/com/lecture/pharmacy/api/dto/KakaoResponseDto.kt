package com.lecture.pharmacy.api.dto

data class KakaoResponseDto(
  val metaDto: MetaDto,

  val documents: List<DocumentDto>
)