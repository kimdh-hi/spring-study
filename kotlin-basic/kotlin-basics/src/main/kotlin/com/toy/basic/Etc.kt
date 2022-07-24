package com.toy.basic

fun main() {
  val testVO = TestVO(null)
  if (testVO.data?.length > 0) {

  }
}

data class TestVO(
  val data: String?
)

