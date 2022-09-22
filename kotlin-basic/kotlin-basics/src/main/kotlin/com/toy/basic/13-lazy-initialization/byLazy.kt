package com.toy.basic.`13-lazy-initialization`

import java.util.concurrent.ThreadLocalRandom

fun toLazyInitFunc(): Int {
  val random = ThreadLocalRandom.current().nextInt(1, 10)
  println("toLazyInitFunc random: $random")
  return random
}

fun main() {
  // 실제 테스트를 사용하는 시점에 초기화하고 싶음
//  val test = toLazyInitFunc()

  /*
  LazyThreadSafetyMode
  - SYNCHRONIZED: thread-safe (default)
  - PUBLICATION: non-thread-safe (쓰레드 세이프하지 않지만 첫번째 반환값만 사용됨)
  - NONE: non-thread-safe

  Thread-safe 에는 항상 추가 비용이 따른다.
  Thread-safe 가 필요없다면 PUBLICATION 으로 지정하여 사용하자.
   */
  val test: Int by lazy(LazyThreadSafetyMode.PUBLICATION) {
    println("init test...")
    toLazyInitFunc()
  }

  for (i in 1..10) {
    Thread {
      println(test) // thread-safe 확인
    }.start()
  }
}

