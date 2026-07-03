package com.study.springmodulith.order

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
  private val orderService: OrderService,
) {

  @PostMapping("/orders")
  fun placeOrder(@RequestParam product: String): Order = orderService.placeOrder(product)
}
