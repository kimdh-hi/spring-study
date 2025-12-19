package com.study.monolithic.order.controller

import com.study.monolithic.order.application.OrderService
import com.study.monolithic.order.controller.dto.PlaceOrderRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
  private val orderService: OrderService,
) {

  @PostMapping("/orders/place")
  fun placeOrder(@RequestBody request: PlaceOrderRequest): ResponseEntity<Unit> {
    orderService.placeOrder(request.toCommand())
    return ResponseEntity.ok().build()
  }
}
