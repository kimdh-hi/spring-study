package com.toy.warmup.warmup

import com.toy.warmup.service.ProductService
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Component
class ProductWarmupTask(
  private val productService: ProductService,
  private val transactionTemplate: TransactionTemplate,
  private val entityManager: EntityManager,
  @Value("\${warmup.product.repeat:2000}") override val repeat: Int,
) : WarmupTask {

  override fun run(iteration: Int) {
    val category = CATEGORIES[iteration % CATEGORIES.size]
    val productId = iteration % PRODUCT_COUNT + 1L

    productService.findTopByCategory(category)
    productService.findById(productId)
    writeAndRollback(category)
  }

  private fun writeAndRollback(category: String) {
    transactionTemplate.executeWithoutResult { status ->
      val product = productService.create("warmup", category, 0)
      productService.update(product.id, "warmup", category, 1)
      entityManager.flush()
      productService.delete(product.id)
      entityManager.flush()
      status.setRollbackOnly()
    }
  }

  companion object {
    private const val PRODUCT_COUNT = 1000
    private val CATEGORIES = listOf("BOOK", "FOOD", "TOY")
  }
}
