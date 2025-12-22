package com.study.monolithic.order.controller.dto

import com.study.monolithic.order.application.dto.CreateOrderCommand

data class CreateOrderRequest(
  val orderItems: List<OrderItem>,
) {
  data class OrderItem(
    val productId: Long,
    val quantity: Long,
  )

  fun toCommand(): CreateOrderCommand {
    val orderItems = this.orderItems.map { CreateOrderCommand.OrderItem(it.productId, it.quantity) }
    return CreateOrderCommand(orderItems)
  }
}
