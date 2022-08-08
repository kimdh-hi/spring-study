package com.toy.proxyex.app.v1

interface OrderServiceV1 {
  fun orderItem(orderItem: String)
}

class OrderServiceV1Impl(
  private val orderRepository: OrderReposiotryV1
): OrderServiceV1 {
  override fun orderItem(orderItem: String) {
    orderRepository.save(orderItem)
  }
}