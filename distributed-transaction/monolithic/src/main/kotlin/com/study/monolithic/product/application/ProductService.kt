package com.study.monolithic.product.application

import com.study.monolithic.product.infra.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
  private val productRepository: ProductRepository,
) {

  @Transactional
  fun buy(productId: Long, quantity: Long): Long {
    val product = productRepository.findByIdOrNull(productId) ?: throw RuntimeException("product not found")
    val totalPrice = product.calculatePrice(quantity)
    product.buy(quantity)

    return totalPrice
  }
}
