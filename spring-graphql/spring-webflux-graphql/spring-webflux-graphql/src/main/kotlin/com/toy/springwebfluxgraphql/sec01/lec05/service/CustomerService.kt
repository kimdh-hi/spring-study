package com.toy.springwebfluxgraphql.sec01.lec05.service

import com.toy.springwebfluxgraphql.sec01.lec05.domain.Customer
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class CustomerService {

  private val customers = Flux.just(
    Customer(1, "kim", 28),
    Customer(2, "kim2", 22),
    Customer(3, "lee", 25),
    Customer(4, "park", 20),
  )

  fun findAll() = customers
}