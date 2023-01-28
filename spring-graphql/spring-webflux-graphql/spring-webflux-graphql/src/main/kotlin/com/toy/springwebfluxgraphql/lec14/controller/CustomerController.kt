package com.toy.springwebfluxgraphql.lec14.controller

import com.toy.springwebfluxgraphql.lec14.exceptions.Errors
import com.toy.springwebfluxgraphql.lec14.service.CustomerService
import com.toy.springwebfluxgraphql.lec14.vo.CustomerDeleteResponseVO
import com.toy.springwebfluxgraphql.lec14.vo.CustomerVO
import graphql.schema.DataFetchingEnvironment
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Controller
class CustomerController(
  private val customerService: CustomerService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @QueryMapping
  fun customers(environment: DataFetchingEnvironment): Flux<CustomerVO> {
    val someHeader = environment.graphQlContext.get<String>("some-header")
    log.info("someHeader: {}", someHeader)
    return customerService.findAll()
  }

  @QueryMapping
  fun customerById(@Argument id: Int): Mono<CustomerVO> = customerService.findById(id)
    .switchIfEmpty { Errors.notFound(id) }


  @MutationMapping
  fun createCustomer(@Argument request: CustomerVO): Mono<CustomerVO> = Mono.just(request)
    .filter { it.age > 19 }
    .flatMap { customerService.create(it) }
    .switchIfEmpty { Errors.ageShouldBeLargerThan19(request) }

  @MutationMapping
  fun updateCustomer(@Argument id: Int, @Argument request: CustomerVO): Mono<CustomerVO>
    = customerService.update(id, request)

  @MutationMapping
  fun deleteCustomer(@Argument id: Int): Mono<CustomerDeleteResponseVO> = customerService.delete(id)
}

/*
query FindAll {
  customers {
    id
    name
    age
    city
  }
}

query FindById {
  customerById(id: 1) {
    id
    name
    age
    city
  }
}

mutation CreateCustomer($request: CustomerSaveRequest!) {
  createCustomer(request: $request) {
    id
    name
    age
    city
  }
}

mutation UpdateCustomer($request: CustomerSaveRequest!) {
  updateCustomer(id: 1, request: $request) {
    id
    name
    age
    city
  }
}

mutation DeleteCustomer {
  deleteCustomer(id: 1) {
    id
    status
  }
}
 */