package com.toy.jpanamedlock.facade

import com.toy.jpanamedlock.repository.StockRepository
import com.toy.jpanamedlock.service.StockService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StockLockFacade(
  private val stockService: StockService,
  private val stockRepository: StockRepository
) {

  @Transactional
  fun decrease(id: Long, quantity: Long) {
    try {
      stockRepository.getLock(id.toString())
      stockService.decrease(id, quantity)
    } finally {
      stockRepository.releaseLock(id.toString())
    }
  }
}