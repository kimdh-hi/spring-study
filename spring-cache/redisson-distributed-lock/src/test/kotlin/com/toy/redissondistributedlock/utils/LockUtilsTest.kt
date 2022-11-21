package com.toy.redissondistributedlock.utils

import com.google.code.tempusfugit.concurrency.ConcurrentRule
import com.google.code.tempusfugit.concurrency.RepeatingRule
import com.google.code.tempusfugit.concurrency.annotations.Concurrent
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.Duration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.math.min


@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class LockUtilsTest(
  private val lockUtils: LockUtils
) {

  @Test
  fun `동시성 제어에 성공한다`() {
    //given
    val lockKey = LockKey.of(
      "test", "test", 10, 10
    )
    val lock = lockUtils.createLock(lockKey)
    var count = 0
    val maxCount = 3

    //when
    val executor = Executors.newFixedThreadPool(3)
    val latch = CountDownLatch(10)
    for (i in 0 until 10) {
      executor.execute {

        latch.countDown()
      }
    }
    latch.await()

    //then
    assertEquals(3, count)
  }

  private fun increaseCount(count: Int, maxCount: Int): Int {
    return min(count+1, maxCount)
  }
}