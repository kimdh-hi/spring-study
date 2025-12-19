package com.study.monolithic.order.infa

import com.study.monolithic.order.domain.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
}
