package com.study.concurrency.stock

import com.study.concurrency.stock.application.DecreaseStrategy
import com.study.concurrency.stock.application.StockDecreaseService
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StockConcurrencyTest {

  @Autowired
  private lateinit var services: List<StockDecreaseService>

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  @DisplayName("NO_LOCK - lost update 로 재고가 0 까지 내려가지 않는다")
  fun noLock() {
    val remaining = decreaseConcurrently(DecreaseStrategy.NO_LOCK)
    assertTrue(remaining > 0, "lost update 가 재현되지 않았습니다. remaining=$remaining")
  }

  @Test
  @DisplayName("SYNCHRONIZED - 재고가 정확히 0 이 된다")
  fun synchronizedLock() {
    assertEquals(0, decreaseConcurrently(DecreaseStrategy.SYNCHRONIZED))
  }

  @Test
  @DisplayName("OPTIMISTIC - 재고가 정확히 0 이 된다")
  fun optimisticLock() {
    assertEquals(0, decreaseConcurrently(DecreaseStrategy.OPTIMISTIC))
  }

  @Test
  @DisplayName("PESSIMISTIC - 재고가 정확히 0 이 된다")
  fun pessimisticLock() {
    assertEquals(0, decreaseConcurrently(DecreaseStrategy.PESSIMISTIC))
  }

  @Test
  @DisplayName("ATOMIC_UPDATE - 재고가 정확히 0 이 된다")
  fun atomicUpdate() {
    assertEquals(0, decreaseConcurrently(DecreaseStrategy.ATOMIC_UPDATE))
  }

  /** 재고 [INITIAL_QUANTITY] 를 만든 뒤 동일 개수의 스레드가 1 씩 차감하고 남은 재고를 반환한다. */
  private fun decreaseConcurrently(strategy: DecreaseStrategy): Long {
    val service = services.single { it.strategy == strategy }
    val stockId = service.create(PRODUCT_ID, INITIAL_QUANTITY)

    val failed = AtomicInteger()
    val executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    val ready = CountDownLatch(INITIAL_QUANTITY.toInt())
    val done = CountDownLatch(INITIAL_QUANTITY.toInt())

    repeat(INITIAL_QUANTITY.toInt()) {
      executor.submit {
        ready.countDown()
        ready.await()
        try {
          service.decrease(stockId, 1)
        } catch (e: Exception) {
          failed.incrementAndGet()
          log.debug("decrease failed. strategy={}", strategy, e)
        } finally {
          done.countDown()
        }
      }
    }
    assertTrue(done.await(60, TimeUnit.SECONDS), "동시 차감이 시간 내에 끝나지 않았습니다. strategy=$strategy")
    executor.shutdown()

    val remaining = service.quantityOf(stockId)
    log.info("strategy={}, remaining={}, failed={}", strategy, remaining, failed.get())
    return remaining
  }

  companion object {
    private const val PRODUCT_ID = 1L
    private const val INITIAL_QUANTITY = 100L
    private const val THREAD_POOL_SIZE = 100
  }
}
