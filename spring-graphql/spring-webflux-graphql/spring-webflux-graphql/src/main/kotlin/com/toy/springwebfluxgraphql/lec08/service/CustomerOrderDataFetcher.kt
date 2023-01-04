package com.toy.springwebfluxgraphql.lec08.service

import com.toy.springwebfluxgraphql.lec08.domain.CustomerWithOrder
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.UnaryOperator

@Service
class CustomerOrderDataFetcher(
  private val customerService: CustomerService,
  private val orderService: OrderService
): DataFetcher<Flux<CustomerWithOrder>> {

  override fun get(environment: DataFetchingEnvironment): Flux<CustomerWithOrder> {
    val containOrders = environment.selectionSet.contains("orders")
    return customerService.findAll()
      .map { CustomerWithOrder.of(it) }
      .transform(updateOrdersTransformer(containOrders))
  }

  private fun updateOrdersTransformer(containOrders: Boolean): UnaryOperator<Flux<CustomerWithOrder>> {
    return if (containOrders) {
      UnaryOperator { f: Flux<CustomerWithOrder> ->
        f.flatMapSequential { fetchOrders(it) }
      }
    } else UnaryOperator {
        f: Flux<CustomerWithOrder> -> f
    }
  }

  private fun fetchOrders(customerWithOrder: CustomerWithOrder): Mono<CustomerWithOrder> {
    return orderService.findByCustomerName(customerWithOrder.name)
      .collectList()
      .doOnNext { customerWithOrder.orders = it }
      .thenReturn(customerWithOrder)
  }
}