package com.study.monolithic.order.controller

import com.study.monolithic.order.application.OrderService
import com.study.monolithic.order.application.RedisLockService
import com.study.monolithic.order.controller.dto.CreateOrderRequest
import com.study.monolithic.order.controller.dto.CreateOrderResponse
import com.study.monolithic.order.controller.dto.PlaceOrderRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
  private val orderService: OrderService,
  private val redisLockService: RedisLockService,
) {

  @PostMapping("/orders")
  fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<CreateOrderResponse> {
    val result = orderService.createOrder(request.toCommand())
    return ResponseEntity.ok(CreateOrderResponse(result.orderId))
  }

  @PostMapping("/orders/place")
  fun placeOrder(@RequestBody request: PlaceOrderRequest): ResponseEntity<Unit> {
    val key = "order:monolithic:${request.orderId}"
    val acquireLock = redisLockService.tryLock(key, request.orderId.toString())
    if (!acquireLock) {
      throw RuntimeException("lock acquisition failed")
    }

    try {
      orderService.placeOrder(request.toCommand())
    } finally {
      redisLockService.releaseLock(key)
    }


    return ResponseEntity.ok().build()
  }
}
