package com.toy.jpanamedlock.facade

import com.toy.jpanamedlock.domain.Stock
import com.toy.jpanamedlock.repository.StockRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest
@Transactional
class StockLockFacadeTest @Autowired constructor(
  private val stockLockFacade: StockLockFacade,
  private val stockRepository: StockRepository
) {
  @BeforeEach
  fun before() {
    val stock = Stock(id = 1L, productId = "p1", quantity = 10)
    stockRepository.save(stock)
  }

  @AfterEach
  fun after() {
    stockRepository.deleteAll()
  }


  @Test
  fun test() {
    val threadCount = 10
    val countDownLatch = CountDownLatch(threadCount)
    val executorService = Executors.newFixedThreadPool(32)
    repeat(threadCount) {
      executorService.submit {
        try {
          stockLockFacade.decrease(1L, 1L)
        } finally {
          countDownLatch.countDown()
        }
      }
    }
    countDownLatch.await()

    val stock = stockRepository.findByIdOrNull(1L)!!
    assertEquals(stock.quantity, 0L)
  }
}