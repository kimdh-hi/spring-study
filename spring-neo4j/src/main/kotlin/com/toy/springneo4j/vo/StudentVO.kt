package com.toy.springneo4j.vo

data class StudentCreateRequestVO(
  val name: String,
  val birthYear: Int,
)

data class StudentCreateResponseVO(
  val id: String,
  val name: String,
  val birthYear: Int,
)