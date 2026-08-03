package com.study.concurrency.stock.infra

import com.study.concurrency.stock.domain.model.Stock
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StockJpaRepository : JpaRepository<Stock, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select s from Stock s where s.id = :id")
  fun findByIdForUpdate(@Param("id") id: Long): Stock?

  //영속성 컨텍스트를 우회하므로 갱신 후 1차 캐시 clear
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
    """
    update Stock s
       set s.quantity = s.quantity - :quantity
     where s.id = :id
       and s.quantity >= :quantity
    """,
  )
  fun decreaseIfEnough(@Param("id") id: Long, @Param("quantity") quantity: Long): Int
}
