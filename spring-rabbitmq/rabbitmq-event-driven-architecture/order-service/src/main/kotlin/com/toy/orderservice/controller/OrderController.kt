package com.toy.orderservice.controller

import com.toy.orderservice.producer.OrderProducer
import com.toy.orderservice.vo.Order
import com.toy.orderservice.vo.OrderEvent
import com.toy.orderservice.vo.OrderStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
  private val orderProducer: OrderProducer
) {

  @PostMapping
  fun order(@RequestBody order: Order): ResponseEntity<String> {
    order.orderId = UUID.randomUUID().toString()
    val orderEvent = OrderEvent(status = OrderStatus.PENDING, message = "order pending ...", order = order)
    orderProducer.sendMessage(orderEvent)

    return ResponseEntity.ok("order...")

  }
}