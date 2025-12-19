package com.study.monolithic.order.controller.dto

import com.study.monolithic.order.application.dto.PlaceOrderCommand

data class PlaceOrderRequest(
  val orderItems: List<OrderItem>,
) {
  data class OrderItem(
    val productId: Long,
    val quantity: Long,
  )

  fun toCommand(): PlaceOrderCommand {
    val orderItems = this.orderItems.map { PlaceOrderCommand.OrderItem(it.productId, it.quantity) }
    return PlaceOrderCommand(orderItems)
  }
}
