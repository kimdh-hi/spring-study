package com.toy.springwebfluxgraphql.lec08.controller

import com.toy.springwebfluxgraphql.lec08.domain.CustomerWithOrder
import com.toy.springwebfluxgraphql.lec08.service.CustomerOrderDataFetcher
import graphql.schema.DataFetchingEnvironment
import graphql.schema.DataFetchingFieldSelectionSet
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux

@Controller
class CustomerController(
  private val customerOrderDataFetcher: CustomerOrderDataFetcher
) {

  @QueryMapping
  fun customers(environment: DataFetchingEnvironment): Flux<CustomerWithOrder>
    = customerOrderDataFetcher.get(environment)
}