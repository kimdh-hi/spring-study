package com.toy.springwebfluxgraphql.sec01.lec05.controller

import com.toy.springwebfluxgraphql.sec01.lec05.domain.Account
import com.toy.springwebfluxgraphql.sec01.lec05.domain.AccountType
import com.toy.springwebfluxgraphql.sec01.lec05.domain.Customer
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@RestController
class AccountController {

  @SchemaMapping
  fun account(customer: Customer): Mono<Account>
    = Mono.just(
      Account(
        id = UUID.randomUUID(),
        amount = ThreadLocalRandom.current().nextInt(1, 1000),
        accountType = if(ThreadLocalRandom.current().nextBoolean()) AccountType.CHECKING else AccountType.SAVING
      )
    )
}