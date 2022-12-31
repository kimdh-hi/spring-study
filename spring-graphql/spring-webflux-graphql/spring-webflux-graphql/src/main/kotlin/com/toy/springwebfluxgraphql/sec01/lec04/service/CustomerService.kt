package com.toy.springwebfluxgraphql.sec01.lec04.service

import com.toy.springwebfluxgraphql.sec01.lec04.domain.Customer
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class CustomerService {

  private val customers = Flux.just(
    Customer(1, "kim", 28, "seoul"),
    Customer(2, "kim2", 22, "seoul"),
    Customer(3, "lee", 25, "bucheon"),
    Customer(4, "park", 20, "busan"),
  )

  fun findAll() = customers
}