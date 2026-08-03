package com.study.monitoring.order.domain.repository

import com.study.monitoring.order.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, String>
