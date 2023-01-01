package com.toy.springwebfluxgraphql.sec01.lec05.controller

import com.toy.springwebfluxgraphql.sec01.lec05.domain.Customer
import com.toy.springwebfluxgraphql.sec01.lec05.service.CustomerService
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class CustomerController(
  private val customerService: CustomerService,
) {

  @QueryMapping
  fun customers(): Flux<Customer> = customerService.findAll()
}