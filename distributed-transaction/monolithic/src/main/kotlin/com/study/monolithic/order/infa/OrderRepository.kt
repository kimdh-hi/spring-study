package com.study.monolithic.order.infa

import com.study.monolithic.order.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
}
