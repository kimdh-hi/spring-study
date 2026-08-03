package com.study.concurrency.stock.domain.repository

import com.study.concurrency.stock.domain.model.Stock

interface StockRepository {

  fun save(stock: Stock): Stock

  fun getById(id: Long): Stock

  fun getByIdForUpdate(id: Long): Stock

  fun decreaseIfEnough(id: Long, quantity: Long): Int
}
