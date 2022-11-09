package com.lecture.issueservice.exception

data class ErrorResponse(
  val code: Int,
  val message: String
)