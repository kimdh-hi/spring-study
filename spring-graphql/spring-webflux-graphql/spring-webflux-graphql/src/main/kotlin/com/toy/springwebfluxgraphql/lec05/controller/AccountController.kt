package com.toy.springwebfluxgraphql.lec05.controller

import com.toy.springwebfluxgraphql.lec05.domain.Account
import com.toy.springwebfluxgraphql.lec05.domain.AccountType
import com.toy.springwebfluxgraphql.lec05.domain.Customer
import graphql.schema.DataFetchingFieldSelectionSet
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@RestController
class AccountController {

  private val log = LoggerFactory.getLogger(javaClass)

  @SchemaMapping
  fun account(
    customer: Customer,
    selectionSet: DataFetchingFieldSelectionSet
  ): Mono<Account> {

    log.info("account => {}", selectionSet.fields)

    return Mono.just(
      Account(
        id = UUID.randomUUID(),
        amount = ThreadLocalRandom.current().nextInt(1, 1000),
        accountType = if(ThreadLocalRandom.current().nextBoolean()) AccountType.CHECKING else AccountType.SAVING
      )
    )
  }
}