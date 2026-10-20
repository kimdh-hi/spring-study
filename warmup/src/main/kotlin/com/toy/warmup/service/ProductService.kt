package com.toy.warmup.service

import com.toy.warmup.domain.Product
import com.toy.warmup.domain.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
  private val productRepository: ProductRepository,
) {

  fun findTopByCategory(category: String): List<Product> =
    productRepository.findTop20ByCategoryOrderByPriceDesc(category)

  fun findById(id: Long): Product? = productRepository.findByIdOrNull(id)

  @Transactional
  fun create(name: String, category: String, price: Long): Product =
    productRepository.save(Product(name, category, price))

  @Transactional
  fun update(id: Long, name: String, category: String, price: Long): Product? =
    productRepository.findByIdOrNull(id)?.apply { update(name, category, price) }

  @Transactional
  fun delete(id: Long): Boolean {
    val product = productRepository.findByIdOrNull(id) ?: return false
    productRepository.delete(product)
    return true
  }
}
