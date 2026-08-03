package com.study.concurrency.stock.application

import com.study.concurrency.stock.domain.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * SELECT ... FOR UPDATE 로 행 배타 락을 획득한 뒤 차감한다.
 * 뒤따르는 트랜잭션은 커밋까지 대기하므로 재시도 없이 항상 성공한다.
 */
@Service
class PessimisticLockStockService(
  stockRepository: StockRepository,
) : AbstractStockDecreaseService(stockRepository) {

  override val strategy = DecreaseStrategy.PESSIMISTIC

  @Transactional
  override fun decrease(stockId: Long, quantity: Long) {
    val stock = stockRepository.getByIdForUpdate(stockId)
    stock.decrease(quantity)
    stockRepository.save(stock)
  }
}
