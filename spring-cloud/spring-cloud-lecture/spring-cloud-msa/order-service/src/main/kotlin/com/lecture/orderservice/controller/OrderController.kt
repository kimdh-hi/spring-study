package com.lecture.orderservice.controller

import com.lecture.orderservice.service.OrderService
import com.lecture.orderservice.vo.OrderResponseVO
import com.lecture.orderservice.vo.OrderSaveRequestVO
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order-service/orders")
class OrderController(
  private val orderService: OrderService,
  private val env: Environment
) {

  @PostMapping("/{userId}")
  fun save(
    @PathVariable userId: String,
    @RequestBody vo: OrderSaveRequestVO
  ): ResponseEntity<OrderResponseVO> {
    vo.userId = userId
    val responseVO = orderService.save(vo)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping("/users/{userId}")
  fun findAllByUserId(@PathVariable userId: String): ResponseEntity<List<OrderResponseVO>> {
    val responseVO = orderService.findAllByUserId(userId)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping("/{orderId}")
  fun findById(@PathVariable orderId: String): ResponseEntity<OrderResponseVO> {
    val responseVO = orderService.findById(orderId)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping("/health-check")
  fun healthCheck() = "order service ok port: ${env.getProperty("local.server.port")}"
}