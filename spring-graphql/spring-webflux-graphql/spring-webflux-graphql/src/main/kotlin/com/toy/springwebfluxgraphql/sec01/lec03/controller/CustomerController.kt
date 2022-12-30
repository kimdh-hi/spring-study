package com.toy.springwebfluxgraphql.sec01.lec03.controller

import com.toy.springwebfluxgraphql.sec01.lec03.domain.Customer
import com.toy.springwebfluxgraphql.sec01.lec03.domain.CustomerOrder
import com.toy.springwebfluxgraphql.sec01.lec03.service.CustomerService
import com.toy.springwebfluxgraphql.sec01.lec03.service.OrderService
import com.toy.springwebfluxgraphql.sec01.lec03.vo.AgeRangeVO
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class CustomerController(
  private val customerService: CustomerService,
  private val orderService: OrderService
) {

  @QueryMapping
  fun customers(): Flux<Customer> = customerService.findAll()

  @SchemaMapping(typeName = "Customer")
  fun orders(customer: Customer): Flux<CustomerOrder> = orderService.findByCustomerName(customer.name)
}