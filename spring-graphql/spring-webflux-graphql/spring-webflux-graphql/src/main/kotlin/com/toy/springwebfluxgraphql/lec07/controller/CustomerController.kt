package com.toy.springwebfluxgraphql.lec07.controller

import com.toy.springwebfluxgraphql.lec07.domain.Customer
import com.toy.springwebfluxgraphql.lec07.domain.CustomerOrder
import com.toy.springwebfluxgraphql.lec07.domain.CustomerWithOrder
import com.toy.springwebfluxgraphql.lec07.service.CustomerOrderFetcher
import com.toy.springwebfluxgraphql.lec07.service.CustomerService
import com.toy.springwebfluxgraphql.lec07.service.OrderService
import graphql.schema.DataFetchingFieldSelectionSet
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux

@Controller
class CustomerController(
  private val customerOrderFetcher: CustomerOrderFetcher
) {

  @QueryMapping
  fun customers(selectionSet: DataFetchingFieldSelectionSet): Flux<CustomerWithOrder>
    = customerOrderFetcher.customerOrders(selectionSet)
}