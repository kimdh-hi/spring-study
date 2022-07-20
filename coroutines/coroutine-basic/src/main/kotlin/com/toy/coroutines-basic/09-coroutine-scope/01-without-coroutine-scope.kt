package com.toy.`coroutines-basic`.`09-coroutine-scope`
import kotlinx.coroutines.delay

// suspend 함수에서 suspend 함수를 호출했지만 비동기로 동작하지 않는다.
// suspend 만 썼다고 알아서 블로킹 시 일시정지가 되는 것이 아님.
// launch, async 등 코루틴 빌더를 통해 만들어진 블럭 내부에서 호출되어야 됨.
suspend fun main() {
  coroutineFunc1()
  println("im main...")
}

suspend fun coroutineFunc1() {
  delay(3000L)
  println("im coroutineFunc...")
}