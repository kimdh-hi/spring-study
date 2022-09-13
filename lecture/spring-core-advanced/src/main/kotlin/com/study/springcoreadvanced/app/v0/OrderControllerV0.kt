package com.study.springcoreadvanced.app.v0

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderControllerV0(
  private val orderServiceV0: OrderServiceV0
) {

  @GetMapping("/v0/request/{id}")
  fun request(@PathVariable id: String): String {
    orderServiceV0.orderItem(id)
    return "ok"
  }
}