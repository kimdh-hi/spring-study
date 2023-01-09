package com.lecture.orderservice.repository

import com.lecture.orderservice.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository: JpaRepository<Order, String> {

  fun findByUserId(userId: String): List<Order>
}