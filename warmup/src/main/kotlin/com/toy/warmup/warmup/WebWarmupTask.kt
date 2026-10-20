package com.toy.warmup.warmup

import com.toy.warmup.api.ProductRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class WebWarmupTask(
  private val environment: Environment,
  @Value("\${warmup.web.repeat:100}") override val repeat: Int,
) : WarmupTask {

  private val client by lazy {
    val port = environment.getRequiredProperty("local.server.port")
    RestClient.builder()
      .baseUrl("http://localhost:$port")
      .defaultHeader(WARMUP_HEADER, "true")
      .defaultStatusHandler(HttpStatusCode::is4xxClientError) { _, _ -> }
      .build()
  }

  override fun run(iteration: Int) {
    val category = CATEGORIES[iteration % CATEGORIES.size]
    val productId = iteration % PRODUCT_COUNT + 1
    val request = ProductRequest("warmup", category, 0)

    client.get().uri("/products?category={category}", category).retrieve().toBodilessEntity()
    client.get().uri("/products/{id}", productId).retrieve().toBodilessEntity()
    client.put().uri("/products/{id}", MISSING_ID).body(request).retrieve().toBodilessEntity()
    client.delete().uri("/products/{id}", MISSING_ID).retrieve().toBodilessEntity()
  }

  companion object {
    const val WARMUP_HEADER = "X-Warmup"
    private const val PRODUCT_COUNT = 1000
    private const val MISSING_ID = -1L
    private val CATEGORIES = listOf("BOOK", "FOOD", "TOY")
  }
}
