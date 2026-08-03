package com.study.concurrency.stock.infra

import com.study.concurrency.stock.domain.model.OptimisticStock
import org.springframework.data.jpa.repository.JpaRepository

interface OptimisticStockJpaRepository : JpaRepository<OptimisticStock, Long>
