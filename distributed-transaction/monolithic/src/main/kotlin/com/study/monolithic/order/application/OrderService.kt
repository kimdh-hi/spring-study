package com.study.monolithic.order.application

import com.study.monolithic.order.application.dto.CreateOrderCommand
import com.study.monolithic.order.application.dto.CreateOrderResult
import com.study.monolithic.order.application.dto.PlaceOrderCommand
import com.study.monolithic.order.domain.Order
import com.study.monolithic.order.domain.OrderItem
import com.study.monolithic.order.infa.OrderItemRepository
import com.study.monolithic.order.infa.OrderRepository
import com.study.monolithic.point.application.PointService
import com.study.monolithic.product.application.ProductService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
  private val orderRepository: OrderRepository,
  private val orderItemRepository: OrderItemRepository,
  private val pointService: PointService,
  private val productService: ProductService,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Transactional
  fun createOrder(command: CreateOrderCommand): CreateOrderResult {
    val order = orderRepository.save(Order.of())
    val orderItems = command.orderItems.map { OrderItem.of(order.id!!, it.productId, it.quantity) }
    orderItemRepository.saveAll(orderItems)

    return CreateOrderResult(order.id!!)
  }

  @Transactional
  fun placeOrder(command: PlaceOrderCommand) {
    val order = orderRepository.findByIdOrNull(command.orderId) ?: throw RuntimeException("Order not found")
    if (order.status == Order.OrderStatus.COMPLETED) return

    val orderItems = orderItemRepository.findAllByOrderId(order.id!!)
    var totalPrice = 0L
    orderItems.forEach {
      val price = productService.buy(it.productId, it.quantity)
      totalPrice += price
    }

    pointService.use(1L, totalPrice)

    order.complete()

    Thread.sleep(3_000L) // for test
    log.debug("order completed")
  }
}
