package com.toy.springwebfluxgraphql.lec06.controller

import com.toy.springwebfluxgraphql.lec06.domain.Customer
import com.toy.springwebfluxgraphql.lec06.domain.CustomerOrder
import com.toy.springwebfluxgraphql.lec06.service.CustomerService
import com.toy.springwebfluxgraphql.lec06.service.OrderService
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux

@Controller
class CustomerController(
  private val customerService: CustomerService,
  private val orderService: OrderService
) {

  // graphql: 6초 소요
  // n+1 과 비슷?한 개념으로 rest 방식보다 많은 시간이 소요됨
  @QueryMapping
  fun customers(): Flux<Customer> = customerService.findAll()

  @SchemaMapping(typeName = "Customer")
  fun orders(customer: Customer): Flux<CustomerOrder>
    = orderService.findByCustomerName(customer.name)
}