package com.toy.springwebfluxgraphql.sec01.lec03.controller

import com.toy.springwebfluxgraphql.sec01.lec03.domain.Customer
import com.toy.springwebfluxgraphql.sec01.lec03.domain.CustomerOrder
import com.toy.springwebfluxgraphql.sec01.lec03.service.CustomerService
import com.toy.springwebfluxgraphql.sec01.lec03.service.OrderService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class CustomerController(
  private val customerService: CustomerService,
  private val orderService: OrderService
) {

  @QueryMapping
  fun customers(): Flux<Customer> = customerService.findAll()

  @SchemaMapping(typeName = "Customer")
  fun orders(customer: Customer, @Argument limit: Long): Flux<CustomerOrder>
    = orderService.findByCustomerName(customer.name)
    .take(limit)
}