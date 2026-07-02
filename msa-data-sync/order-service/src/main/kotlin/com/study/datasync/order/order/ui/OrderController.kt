package com.study.datasync.order.order.ui

import com.study.datasync.order.order.application.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class CreateOrderRequest(
  val product: String,
  val amount: Int,
)

data class OrderResponse(
  val id: String?,
  val product: String,
  val amount: Int,
)

@RestController
@RequestMapping("/api/orders")
class OrderController(
  private val orderService: OrderService,
) {

  @PostMapping
  fun create(@RequestBody request: CreateOrderRequest): OrderResponse {
    val order = orderService.create(request.product, request.amount)
    return OrderResponse(order.id, order.product, order.amount)
  }

  @GetMapping
  fun list(): List<OrderResponse> =
    orderService.findAll().map { OrderResponse(it.id, it.product, it.amount) }
}
