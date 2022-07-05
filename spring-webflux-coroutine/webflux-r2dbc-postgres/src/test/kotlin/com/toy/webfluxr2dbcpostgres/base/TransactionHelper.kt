package com.toy.webfluxr2dbcpostgres.base

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class TransactionHelper @Autowired constructor(rxtx: TransactionalOperator?) {
  companion object {
    private var rxtx: TransactionalOperator? = null
    public fun <T> withRollback(publisher: Mono<T?>?): Mono<T?> {
      return rxtx!!.execute { tx: ReactiveTransaction ->
        tx.setRollbackOnly()
        publisher!!
      }
        .next()
    }

    public fun <T> withRollback(publisher: Flux<T?>?): Flux<T> {
      return rxtx!!.execute { tx: ReactiveTransaction ->
        tx.setRollbackOnly()
        publisher!!
      }
    }
  }

  init {
    Companion.rxtx = rxtx
  }
}