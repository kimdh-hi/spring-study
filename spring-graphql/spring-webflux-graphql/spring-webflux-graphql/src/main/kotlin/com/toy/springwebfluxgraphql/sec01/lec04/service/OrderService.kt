package com.toy.springwebfluxgraphql.sec01.lec04.service

import com.toy.springwebfluxgraphql.sec01.lec04.domain.CustomerOrder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class OrderService {

  private val log = LoggerFactory.getLogger(javaClass)

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

  fun findByCustomerNames(names: List<String>): Flux<List<CustomerOrder>>{

    log.info("findByCustomerNames called")

    return Flux.fromIterable(names)
      .flatMapSequential { getOrders(it).defaultIfEmpty(emptyList()) }

//    return Flux.fromIterable(names)
//      .map { orders.getOrDefault(it, listOf()) }
  }

  private fun getOrders(name: String) = Mono.justOrEmpty(orders[name])
}