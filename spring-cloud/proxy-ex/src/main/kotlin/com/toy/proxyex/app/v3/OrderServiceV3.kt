package com.toy.proxyex.app.v3

import org.springframework.stereotype.Service

@Service
class OrderServiceV3(
  private val orderRepository: OrderRepositoryV3
) {

  fun orderItem(orderItem: String) {
    orderRepository.save(orderItem)
  }
}