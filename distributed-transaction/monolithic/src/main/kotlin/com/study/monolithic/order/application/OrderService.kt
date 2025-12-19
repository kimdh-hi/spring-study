package com.study.monolithic.order.application

import com.study.monolithic.order.application.dto.PlaceOrderCommand
import com.study.monolithic.order.domain.Order
import com.study.monolithic.order.domain.OrderItem
import com.study.monolithic.order.infa.OrderItemRepository
import com.study.monolithic.order.infa.OrderRepository
import com.study.monolithic.point.application.PointService
import com.study.monolithic.product.application.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
  private val orderRepository: OrderRepository,
  private val orderItemRepository: OrderItemRepository,
  private val pointService: PointService,
  private val productService: ProductService,
) {

  @Transactional
  fun placeOrder(command: PlaceOrderCommand) {
    val order = orderRepository.save(Order.of())
    var totalPrice = 0L
    command.orderItems.forEach {
      val orderItem = orderItemRepository.save(OrderItem.of(order.id!!, it.productId, it.quantity))
      val price = productService.buy(it.productId, orderItem.quantity)
      totalPrice += price
    }

    pointService.use(1L, totalPrice)
  }
}
