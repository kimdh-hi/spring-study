package com.study.sqsconsumer.domain.repository

import com.study.sqsconsumer.domain.model.ProcessedOrder
import org.springframework.data.jpa.repository.JpaRepository

interface ProcessedOrderRepository : JpaRepository<ProcessedOrder, Long>
