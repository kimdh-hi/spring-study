package com.toy.basic.`00-anti`

/*
불변컬력션을 가변컬렉션 타입으로 변환하여 사용하지 않는다.
컴파일 시점에 에러를 발생시키긴 하지만 플랫폼에 따라 그렇지 않은 경우도 있다고 한다.

불변컬렉션을 가변컬렉션으로 변환해야 한다면 복사 하여 사용한다. toMutableList() ...
 */
fun main() {
  val immutableList2 = listOf(1, 2, 3)
  val mutableList2 = immutableList2.toMutableList()
  mutableList2.add(4)
  println(mutableList2)

  val immutableList1 = listOf(1, 2, 3)
  if (immutableList1 is MutableList) { // anti-pattern
    immutableList1.add(4)
  }
}