package com.lecture.userservice.feign

import com.lecture.userservice.vo.OrderResponseVO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "order-service")
interface OrderServiceClient {

  @GetMapping("/order-service/{userId}/orders")
  fun findById(@PathVariable userId: String): List<OrderResponseVO>
}