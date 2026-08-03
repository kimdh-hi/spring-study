package com.study.concurrency.stock.application

interface StockDecreaseService {

  val strategy: DecreaseStrategy

  fun create(productId: Long, quantity: Long): Long

  fun quantityOf(stockId: Long): Long

  fun decrease(stockId: Long, quantity: Long)
}
