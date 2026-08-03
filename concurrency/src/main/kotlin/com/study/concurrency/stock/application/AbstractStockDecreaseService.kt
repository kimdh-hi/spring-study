package com.study.concurrency.stock.application

import com.study.concurrency.stock.domain.model.Stock
import com.study.concurrency.stock.domain.repository.StockRepository
import org.springframework.transaction.annotation.Transactional

/**
 * `Stock`(버전 없음) 을 사용하는 전략들의 공통 조회/생성 구현.
 * 프록시가 트랜잭션을 적용할 수 있도록 메서드를 open 으로 선언한다.
 */
abstract class AbstractStockDecreaseService(
  protected val stockRepository: StockRepository,
) : StockDecreaseService {

  @Transactional
  override fun create(productId: Long, quantity: Long): Long =
    requireNotNull(stockRepository.save(Stock.of(productId, quantity)).id)

  @Transactional(readOnly = true)
  override fun quantityOf(stockId: Long): Long = stockRepository.getById(stockId).quantity
}
