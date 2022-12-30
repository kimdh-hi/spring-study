package com.toy.springwebfluxgraphql.sec01.lec03.service

import com.toy.springwebfluxgraphql.sec01.lec03.domain.CustomerOrder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.*

@Service
class OrderService {

  companion object {
    val orders = mapOf(
      "kim" to listOf(
        CustomerOrder(UUID.randomUUID(), "kim-product1"),
        CustomerOrder(UUID.randomUUID(), "kim-product2"),
      ),
      "lee" to listOf(
        CustomerOrder(UUID.randomUUID(), "lee-product1"),
        CustomerOrder(UUID.randomUUID(), "lee-product2"),
      ),
    )
  }

  fun findByCustomerName(name: String): Flux<CustomerOrder> = Flux.fromIterable(orders.getOrDefault(name, listOf()))
}