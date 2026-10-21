package com.toy.warmup.warmup

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class WebWarmupTask(
  private val environment: Environment,
) : WarmupTask {

  private val client by lazy {
    val port = environment.getRequiredProperty("local.server.port")
    RestClient.create("http://localhost:$port")
  }

  override fun run() = repeat(REPEAT, ::call)

  private fun call(iteration: Int) {
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
