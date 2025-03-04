package com.toy.springjunitmultithread.`1-test`

import com.toy.springjunitmultithread.base.MyCounter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class Test1 {

  @Test
  fun `동시성이 없는 경우`() {
    //given
    val counter = MyCounter()

    //when
    for (i in 0 until 100) {
      counter.increment()
    }

    //then
    assertEquals(100, counter.count)
  }

  @Test
  fun `멀티쓰레드 요청으로 인한 동시성 문제 발생 - 비결정적으로 테스트 성공`() {
    //given
    val threadCount = 150
    val executorService = Executors.newFixedThreadPool(threadCount)
    val latch = CountDownLatch(threadCount)

    val counter = MyCounter()

    //when
    for(i in 0 until threadCount) {
      executorService.execute {
        counter.increment()
        latch.countDown()
      }
    }
    latch.await()

    //then
    println(counter.count)
    assertNotEquals(threadCount, counter.count)
  }


}