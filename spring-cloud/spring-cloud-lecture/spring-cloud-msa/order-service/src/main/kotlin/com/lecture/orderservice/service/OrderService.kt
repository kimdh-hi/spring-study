package com.lecture.orderservice.service

import com.lecture.orderservice.repository.OrderRepository
import com.lecture.orderservice.vo.OrderResponseVO
import com.lecture.orderservice.vo.OrderSaveRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderService(
  private val orderRepository: OrderRepository
) {

  @Transactional
  fun save(vo: OrderSaveRequestVO): OrderResponseVO {
    vo.calculateTotalPrice()
    val order = vo.toEntity()
    return OrderResponseVO.of(orderRepository.save(order))
  }

  fun findAllByUserId(userId: String): List<OrderResponseVO> {
    return orderRepository.findByUserId(userId)
      .map { OrderResponseVO.of(it) }
  }

  fun findById(orderId: String): OrderResponseVO {
    val order = orderRepository.findByIdOrNull(orderId)
      ?: throw RuntimeException("order not found")
    return OrderResponseVO.of(order)
  }
}