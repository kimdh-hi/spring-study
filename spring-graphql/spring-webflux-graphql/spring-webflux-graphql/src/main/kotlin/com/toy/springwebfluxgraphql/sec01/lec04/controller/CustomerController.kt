package com.toy.springwebfluxgraphql.sec01.lec04.controller

import com.toy.springwebfluxgraphql.sec01.lec04.domain.Customer
import com.toy.springwebfluxgraphql.sec01.lec04.domain.CustomerOrder
import com.toy.springwebfluxgraphql.sec01.lec04.service.CustomerService
import com.toy.springwebfluxgraphql.sec01.lec04.service.OrderService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
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

  /**
   * N+1 문제 해결
   *
   * 특정 type 내에 list 형태 type 이 존재하는 경우 해당 list 를 조회하기 위해 쿼리가 한 개 더 나가는 것
   * 기존 Customer -> CustomerOrder
   *
   * @BatchMapping 사용시 typeName 에서 지정한 부모타입? 에서 추가적으로 필요한? type 을 list 타입으로 받을 수 있음
   *
   * 기존에는 @SchemaMapping 로 부모타입을 단 건으로 매핑받았음.
   */
  @BatchMapping(typeName = "Customer")
  fun orders(customers: List<Customer>): Flux<List<CustomerOrder>>
    = orderService.findByCustomerNames(customers.map { it.name })
}

/**
 * ERROR: The size of the promised values MUST be the same size as the key list
 *
 * 위 샘플의 경우 customers 의 size 와 결과로 반환된 List<CustomerOrder> 가 다른 경우 발생
 */