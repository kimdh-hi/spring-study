package com.toy.springwebfluxgraphql.lec06.controller

import com.toy.springwebfluxgraphql.lec06.domain.CustomerWithOrder
import com.toy.springwebfluxgraphql.lec06.service.CustomerService
import com.toy.springwebfluxgraphql.lec06.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class CustomerRestController(
  private val customerService: CustomerService,
  private val orderService: OrderService
) {

  // rest api: 4초 소요
  @GetMapping("/rest-customers")
  fun findCustomers(): Flux<CustomerWithOrder> {
    return customerService.findAll()
      .flatMap { customer ->
        orderService.findByCustomerName(customer.name)
          .collectList()
          .map { CustomerWithOrder.of(customer, it) }
      }
  }
}