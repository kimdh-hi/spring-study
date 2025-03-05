package com.toy.springtest.dto

data class UserRequest(
  val name: String,
  val age: Int,
) {
  init {
    require(age > 0)
  }
}
