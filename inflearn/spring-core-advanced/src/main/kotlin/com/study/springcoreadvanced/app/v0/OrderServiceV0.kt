package com.study.springcoreadvanced.app.v0

import org.springframework.stereotype.Service

@Service
class OrderServiceV0(
  private val orderRepositoryV0: OrderRepositoryV0
) {

  fun orderItem(id: String) = orderRepositoryV0.save(id)
}