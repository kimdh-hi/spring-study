package com.toy.springrsocket.dto

data class ComputationRequestDto(
  val input: Int
)

data class ComputationResponseDto(
  val input: Int,
  val output: Int
)

data class ChartResponseDto(
  val input: Int = 0,
  val output: Int = 0
)