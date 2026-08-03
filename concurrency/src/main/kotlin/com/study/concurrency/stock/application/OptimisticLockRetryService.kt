package com.study.concurrency.stock.application

import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service

/**
 * 낙관적 락 충돌 재시도 파사드.
 * `@Transactional` 을 붙이지 않아 매 시도마다 새 트랜잭션·새 영속성 컨텍스트로 재조회한다.
 */
@Service
class OptimisticLockRetryService(
  private val optimisticLockStockService: OptimisticLockStockService,
) : StockDecreaseService {

  override val strategy = DecreaseStrategy.OPTIMISTIC

  override fun create(productId: Long, quantity: Long): Long =
    optimisticLockStockService.create(productId, quantity)

  override fun quantityOf(stockId: Long): Long = optimisticLockStockService.quantityOf(stockId)

  override fun decrease(stockId: Long, quantity: Long) {
    repeat(MAX_ATTEMPTS) {
      try {
        optimisticLockStockService.decrease(stockId, quantity)
        return
      } catch (e: OptimisticLockingFailureException) {
        Thread.sleep(BACKOFF_MILLIS)
      }
    }
    throw IllegalStateException("optimistic lock retry exhausted. id=$stockId, attempts=$MAX_ATTEMPTS")
  }

  companion object {
    private const val MAX_ATTEMPTS = 500
    private const val BACKOFF_MILLIS = 3L
  }
}
