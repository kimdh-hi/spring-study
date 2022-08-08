package com.toy.proxyex.app.v2

class OrderServiceV2(
  private val orderRepository: OrderRepositoryV2
) {

  fun orderItem(orderItem: String) {
    orderRepository.save(orderItem)
  }
}