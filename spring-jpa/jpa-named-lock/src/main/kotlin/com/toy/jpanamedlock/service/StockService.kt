package com.toy.jpanamedlock.service

import com.toy.jpanamedlock.repository.StockRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockService(
  private val stockRepository: StockRepository
) {

  @Transactional
  fun decrease(id: Long, quantity: Long) {
    val stock = stockRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("not found")
    stock.decrease(quantity)
  }
}