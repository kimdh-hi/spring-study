package com.study.concurrency.stock.infra

import com.study.concurrency.stock.domain.model.OptimisticStock
import com.study.concurrency.stock.domain.repository.OptimisticStockRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OptimisticStockRepositoryAdapter(
  private val optimisticStockJpaRepository: OptimisticStockJpaRepository,
) : OptimisticStockRepository {

  override fun save(stock: OptimisticStock): OptimisticStock = optimisticStockJpaRepository.save(stock)

  override fun getById(id: Long): OptimisticStock =
    optimisticStockJpaRepository.findByIdOrNull(id)
      ?: throw NoSuchElementException("optimistic stock not found. id=$id")
}
