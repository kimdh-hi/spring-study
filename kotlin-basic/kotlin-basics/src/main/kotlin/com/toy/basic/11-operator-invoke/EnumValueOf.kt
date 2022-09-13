package com.toy.basic.`11-operator-invoke`

fun main() {
  val t1 = TestEnum.of("test1")
  val t2 = TestEnum("test2") // operator - invoke 방식 (마치 생성자처럼 사용 가능)

  println(t1)
  println(t2)
}

enum class TestEnum {
  TEST1, TEST2, TEST3;

  companion object {
    fun of(value: String) = valueOf(value.uppercase())
    operator fun invoke(value: String) = valueOf(value.uppercase())
  }
}