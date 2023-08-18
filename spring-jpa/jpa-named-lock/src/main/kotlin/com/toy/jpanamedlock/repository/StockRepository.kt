package com.toy.jpanamedlock.repository

import com.toy.jpanamedlock.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StockRepository: JpaRepository<Stock, Long> {

  @Query(value = "select get_lock(:key, 1000)", nativeQuery = true)
  fun getLock(key: String)

  @Query(value = "select release_lock(:key)", nativeQuery = true)
  fun releaseLock(key: String)
}