package com.toy.springmicrometer_grafana_prometheus.controller

import com.toy.springmicrometer_grafana_prometheus.order.OrderService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
  private val orderService: OrderService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/order")
  fun order() = ResponseEntity.ok(orderService.order())

  @GetMapping("/cancel")
  fun cancel() = ResponseEntity.ok(orderService.cancel())

  @GetMapping("/stock")
  fun stock() = ResponseEntity.ok(orderService.getStock().get())
}