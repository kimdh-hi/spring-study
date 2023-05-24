package com.lecture.service

import com.lecture.client.ProductClient
import com.lecture.client.UserClient
import com.lecture.dto.OrderRequestDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class OrderService(
  private val productClient: ProductClient,
  private val userClient: UserClient
) {

  fun order(dto: Mono<OrderRequestDto>) {

  }
}