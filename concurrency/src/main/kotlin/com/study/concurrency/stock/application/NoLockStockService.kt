package com.study.concurrency.stock.application

import com.study.concurrency.stock.domain.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 기준선 — 락 없이 조회 → 차감 → 저장.
 * 두 트랜잭션이 같은 quantity 를 읽으면 나중 커밋이 앞선 커밋을 덮어써 lost update 가 발생한다.
 */
@Service
class NoLockStockService(
  stockRepository: StockRepository,
) : AbstractStockDecreaseService(stockRepository) {

  override val strategy = DecreaseStrategy.NO_LOCK

  @Transactional
  override fun decrease(stockId: Long, quantity: Long) {
    val stock = stockRepository.getById(stockId)
    stock.decrease(quantity)
    stockRepository.save(stock)
  }
}
