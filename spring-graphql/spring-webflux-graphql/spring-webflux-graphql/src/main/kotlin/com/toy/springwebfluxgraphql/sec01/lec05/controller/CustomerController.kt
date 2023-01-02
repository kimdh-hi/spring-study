package com.toy.springwebfluxgraphql.sec01.lec05.controller

import com.toy.springwebfluxgraphql.sec01.lec05.domain.Customer
import com.toy.springwebfluxgraphql.sec01.lec05.service.CustomerService
import graphql.schema.DataFetcherFactoryEnvironment
import graphql.schema.DataFetchingEnvironment
import graphql.schema.DataFetchingFieldSelectionSet
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class CustomerController(
  private val customerService: CustomerService,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @QueryMapping
  fun customers(
    environment: DataFetchingEnvironment,
    selectionSet: DataFetchingFieldSelectionSet
  ): Flux<Customer> {
    log.info("environment.document => {}", environment.document)
    log.info("customers.selectionSet => {}", selectionSet.fields)
    return customerService.findAll()
  }
}