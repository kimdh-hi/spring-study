package com.toy.springwebfluxgraphql.lec06.service

import com.toy.springwebfluxgraphql.lec06.domain.Customer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.LocalDateTime

@Service
class CustomerService {

  private val log = LoggerFactory.getLogger(javaClass)

  private val customers = Flux.just(
    Customer(1, "kim", 28, "seoul"),
    Customer(2, "kim2", 22, "seoul"),
    Customer(3, "lee", 25, "bucheon"),
    Customer(4, "park", 20, "busan"),
  )

  fun findAll() = customers
    .delayElements(Duration.ofSeconds(1))
    .doOnNext { log.info("${LocalDateTime.now()}:  customer ${it.name}") }
}