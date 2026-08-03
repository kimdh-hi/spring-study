package com.study.concurrency.stock.application

import com.study.concurrency.stock.domain.model.OptimisticStock
import com.study.concurrency.stock.domain.repository.OptimisticStockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * `@Version` 기반 낙관적 락.
 * 커밋 시 `where id = ? and version = ?` 조건이 걸리며, 갱신 행이 0 이면 충돌로 판단해 예외를 던진다.
 * 재시도는 새 트랜잭션·새 영속성 컨텍스트에서 재조회해야 하므로 [OptimisticLockRetryService] 가 담당한다.
 */
@Service
class OptimisticLockStockService(
  private val optimisticStockRepository: OptimisticStockRepository,
) {

  @Transactional
  fun create(productId: Long, quantity: Long): Long =
    requireNotNull(optimisticStockRepository.save(OptimisticStock.of(productId, quantity)).id)

  @Transactional(readOnly = true)
  fun quantityOf(stockId: Long): Long = optimisticStockRepository.getById(stockId).quantity

  @Transactional
  fun decrease(stockId: Long, quantity: Long) {
    val stock = optimisticStockRepository.getById(stockId)
    stock.decrease(quantity)
    optimisticStockRepository.save(stock)
  }
}
