package com.toy.springwebfluxgraphql.lec13.controller

import com.toy.springwebfluxgraphql.lec13.service.CustomerEventService
import com.toy.springwebfluxgraphql.lec13.vo.CustomerEvent
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux

@Controller
class CustomerEventController(
  private val customerEventService: CustomerEventService
) {

  @SubscriptionMapping
  fun customerEvents(): Flux<CustomerEvent> = customerEventService.subscribe()
}