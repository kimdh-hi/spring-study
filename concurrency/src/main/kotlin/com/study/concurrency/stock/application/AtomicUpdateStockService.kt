package com.study.concurrency.stock.application

import com.study.concurrency.stock.domain.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * `update stock set quantity = quantity - ? where id = ? and quantity >= ?` 단일 쿼리로 차감한다.
 * 읽기와 쓰기가 하나의 원자적 연산이라 락 대기도 재시도도 필요 없다.
 */
@Service
class AtomicUpdateStockService(
  stockRepository: StockRepository,
) : AbstractStockDecreaseService(stockRepository) {

  override val strategy = DecreaseStrategy.ATOMIC_UPDATE

  @Transactional
  override fun decrease(stockId: Long, quantity: Long) {
    val updated = stockRepository.decreaseIfEnough(stockId, quantity)
    require(updated > 0) { "stock is not enough. id=$stockId, amount=$quantity" }
  }
}
