package com.study.concurrency.stock.domain.repository

import com.study.concurrency.stock.domain.model.OptimisticStock

interface OptimisticStockRepository {

  fun save(stock: OptimisticStock): OptimisticStock

  fun getById(id: Long): OptimisticStock
}
