package com.toy.warmup.warmup

import com.toy.warmup.service.ProductService
import org.springframework.stereotype.Component

@Component
class ProductWarmupTask(
  private val productService: ProductService,
) : WarmupTask {

  override fun run() = repeat(REPEAT, ::call)

  private fun call(iteration: Int) {
    val category = CATEGORIES[iteration % CATEGORIES.size]
    val productId = iteration % PRODUCT_COUNT + 1L

    productService.findTopByCategory(category)
    productService.findById(productId)
  }

  companion object {
    private const val REPEAT = 2000
    private const val PRODUCT_COUNT = 1000
    private val CATEGORIES = listOf("BOOK", "FOOD", "TOY")
  }
}
