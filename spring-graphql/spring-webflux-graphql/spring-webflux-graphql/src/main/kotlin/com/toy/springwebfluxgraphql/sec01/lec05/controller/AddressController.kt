package com.toy.springwebfluxgraphql.sec01.lec05.controller

import com.toy.springwebfluxgraphql.sec01.lec05.domain.Address
import com.toy.springwebfluxgraphql.sec01.lec05.domain.Customer
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class AddressController {

  @SchemaMapping(typeName = "Customer")
  fun address(customer: Customer): Mono<Address>
    = Mono.just(Address(street = "${customer.name} street", city = "${customer.name} city"))
}