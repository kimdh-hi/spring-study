package com.study.springcoreadvanced.v0.app

import org.springframework.stereotype.Service

@Service
class OrderServiceV0(
  private val orderRepositoryV0: OrderRepositoryV0
) {

  fun orderItem(id: String) = orderRepositoryV0.save(id)
}