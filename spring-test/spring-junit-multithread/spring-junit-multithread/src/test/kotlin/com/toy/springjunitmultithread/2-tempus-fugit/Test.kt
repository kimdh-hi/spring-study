package com.toy.springjunitmultithread.`2-tempus-fugit`

import com.google.code.tempusfugit.concurrency.ConcurrentRule
import com.google.code.tempusfugit.concurrency.RepeatingRule
import com.google.code.tempusfugit.concurrency.annotations.Concurrent
import com.google.code.tempusfugit.concurrency.annotations.Repeating
import com.toy.springjunitmultithread.base.MyCounter
import org.junit.AfterClass
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Nested


class Test {

  @JvmField
  @Rule
  var concurrently = ConcurrentRule()

  @JvmField
  @Rule
  var rule = RepeatingRule()

  companion object {
    val counter = MyCounter()

    @JvmStatic
    @AfterClass
    fun after() {
      println(counter.count)
    }
  }

  // 비결정적이지만 꽤 높은 확률로 공유자원에 대한 경합상태가 발생함
  @Test
  @Concurrent(count = 10)
  @Repeating(repetition = 20)
  fun test() {
    counter.increment()
  }
}