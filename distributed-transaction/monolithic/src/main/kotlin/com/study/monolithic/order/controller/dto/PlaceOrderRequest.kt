package com.study.monolithic.order.controller.dto

import com.study.monolithic.order.application.dto.PlaceOrderCommand

data class PlaceOrderRequest(
  val orderId: Long
) {
  fun toCommand(): PlaceOrderCommand {
    return PlaceOrderCommand(orderId)
  }
}
