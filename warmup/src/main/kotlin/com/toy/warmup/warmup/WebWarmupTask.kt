package com.toy.warmup.warmup

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class WebWarmupTask(
  private val environment: Environment,
) : WarmupTask {

  override fun run() {
    val port = environment.getRequiredProperty("local.server.port")
    val client = RestClient.create("http://localhost:$port")
    repeat(REPEAT) { call(client, it) }
  }

  private fun call(client: RestClient, iteration: Int) {
    val category = CATEGORIES[iteration % CATEGORIES.size]
    val productId = iteration % PRODUCT_COUNT + 1

    client.get().uri("/products?category={category}", category).retrieve().toBodilessEntity()
    client.get().uri("/products/{id}", productId).retrieve().toBodilessEntity()
  }

  companion object {
    private const val REPEAT = 100
    private const val PRODUCT_COUNT = 1000
    private val CATEGORIES = listOf("BOOK", "FOOD", "TOY")
  }
}
