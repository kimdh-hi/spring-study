package com.toy.springwebfluxgraphql.lec06.service

import com.toy.springwebfluxgraphql.lec06.domain.CustomerWithOrder
import graphql.schema.DataFetchingFieldSelectionSet
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.UnaryOperator


@Service
class CustomerOrderFetcher(
  private val customerService: CustomerService,
  private val orderService: OrderService
) {

  fun customerOrders(selectionSet: DataFetchingFieldSelectionSet): Flux<CustomerWithOrder> {
    val containOrders = selectionSet.contains("orders")
    return customerService.findAll()
      .map { CustomerWithOrder.of(it) }
      .transform(updateOrdersTransformer(containOrders))
  }

  private fun updateOrdersTransformer(containOrders: Boolean): UnaryOperator<Flux<CustomerWithOrder>> {
    return if (containOrders) {
      UnaryOperator { f: Flux<CustomerWithOrder> ->
        f.flatMap { fetchOrders(it) }
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