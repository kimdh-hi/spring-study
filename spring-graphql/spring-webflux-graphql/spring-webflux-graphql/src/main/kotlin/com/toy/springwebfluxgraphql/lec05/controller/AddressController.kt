package com.toy.springwebfluxgraphql.lec05.controller

import com.toy.springwebfluxgraphql.lec05.domain.Address
import com.toy.springwebfluxgraphql.lec05.domain.Customer
import graphql.schema.DataFetchingFieldSelectionSet
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class AddressController {

  private val log = LoggerFactory.getLogger(javaClass)

  @SchemaMapping(typeName = "Customer")
  fun address(
    customer: Customer,
    selectionSet: DataFetchingFieldSelectionSet
  ): Mono<Address> {

    log.info("address => {}", selectionSet.fields)

    return Mono.just(Address(street = "${customer.name} street", city = "${customer.name} city"))
  }
}