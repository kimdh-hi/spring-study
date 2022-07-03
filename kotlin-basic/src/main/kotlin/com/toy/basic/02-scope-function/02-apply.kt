package com.toy.basic.`02-scope-function`

/**
 * 프로퍼티 set 시 가독성있는 코드 작성 가능
 */
fun main() {
  val testClass = TestClass(data1 = "data1", data2 = "data2")

  testClass.apply {
    data1 = "updateData1"
    data2 = "updateData2"
  }

  println(testClass)
}

data class TestClass (
  var data1: String,
  var data2: String
)