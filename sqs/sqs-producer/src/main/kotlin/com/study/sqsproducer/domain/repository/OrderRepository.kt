package com.study.sqsproducer.domain.repository

import com.study.sqsproducer.domain.model.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>
