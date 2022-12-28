package com.toy.springwebfluxgraphql.sec01.lec02.service

import com.toy.springwebfluxgraphql.sec01.lec02.domain.Customer
import com.toy.springwebfluxgraphql.sec01.lec02.vo.AgeRangeVO
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CustomerService {

  private val customers = Flux.just(
    Customer(1, "kim", 28, "seoul"),
    Customer(2, "kim2", 22, "seoul"),
    Customer(3, "lee", 25, "bucheon"),
    Customer(4, "park", 20, "busan"),
  )

  fun findAll() = customers

  fun findById(id: Long): Mono<Customer?> = customers
    .filter { it.id == id }
    .next()

  fun findByNameContains(name: String): Flux<Customer> = customers
    .filter { it.name.contains(name) }

  fun findByAgeBetween(ageRange: AgeRangeVO): Flux<Customer> = customers
    .filter { it.age in ageRange.minAge .. ageRange.maxAge }
}