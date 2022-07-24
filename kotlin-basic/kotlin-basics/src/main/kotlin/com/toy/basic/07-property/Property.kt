package com.toy.basic.`07-property`

fun main() {
  val vo = TestVO(1L, "testName", "test")
  println(vo.id)
  println(vo.name)

  // vo.immutableValue = "asd" // 직접 필드에 대한 setter 불가
  vo.updateImmutableValue("update") // 메서드를 통해 클래스 내부에서는 값 갱신 가능
  println(vo.immutableValue)
}

class TestVO(
  id: Long,
  name: String,
  immutableValue: String
) {
  val id: Long = id

  val _id: String
    get() = "asd"

  val name: String
    get() = "nameyo"

  val immutableValue: String
    get() = _immutableValue

  private var _immutableValue: String = immutableValue

  fun updateImmutableValue(value: String) {
    _immutableValue = value
  }
}