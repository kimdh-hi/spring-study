package com.study.sqsproducer.application

import com.study.sqsproducer.domain.model.Order
import com.study.sqsproducer.domain.repository.OrderEventPublisher
import com.study.sqsproducer.domain.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
  private val orderRepository: OrderRepository,
  private val orderEventPublisher: OrderEventPublisher,
) {
  @Transactional
  fun place(productName: String, quantity: Int, customerId: String): Order {
    val order = orderRepository.save(Order(productName, quantity, customerId))
    orderEventPublisher.publish(order)
    return order
  }

  @Transactional
  fun placeOrdered(productName: String, quantity: Int, customerId: String): Order {
    val order = orderRepository.save(Order(productName, quantity, customerId))
    orderEventPublisher.publishOrdered(order)
    return order
  }
}
