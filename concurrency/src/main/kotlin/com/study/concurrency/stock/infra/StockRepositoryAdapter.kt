package com.study.concurrency.stock.infra

import com.study.concurrency.stock.domain.model.Stock
import com.study.concurrency.stock.domain.repository.StockRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class StockRepositoryAdapter(
  private val stockJpaRepository: StockJpaRepository,
) : StockRepository {

  override fun save(stock: Stock): Stock = stockJpaRepository.save(stock)

  override fun getById(id: Long): Stock =
    stockJpaRepository.findByIdOrNull(id) ?: throw NoSuchElementException("stock not found. id=$id")

  override fun getByIdForUpdate(id: Long): Stock =
    stockJpaRepository.findByIdForUpdate(id) ?: throw NoSuchElementException("stock not found. id=$id")

  override fun decreaseIfEnough(id: Long, quantity: Long): Int =
    stockJpaRepository.decreaseIfEnough(id, quantity)
}
