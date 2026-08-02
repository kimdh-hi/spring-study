package com.study.springelasticsearch.product.application

import com.study.springelasticsearch.product.domain.model.Product
import com.study.springelasticsearch.product.domain.model.ProductSearchResult
import com.study.springelasticsearch.product.domain.repository.ProductRepository
import com.study.springelasticsearch.product.domain.repository.ProductSearchRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
  private val productRepository: ProductRepository,
  private val productSearchRepository: ProductSearchRepository,
) {

  @Transactional
  fun register(name: String, description: String, category: String, price: Int): Product {
    val product = productRepository.save(Product.of(name, description, category, price))
    productSearchRepository.index(product)
    return product
  }

  @Transactional(readOnly = true)
  fun searchByLike(keyword: String): List<Product> =
    productRepository.findByNameContainingOrDescriptionContaining(keyword, keyword)

  fun search(keyword: String, size: Int = 10): List<ProductSearchResult> =
    productSearchRepository.search(keyword, size)

  fun categoryFacet(keyword: String): Map<String, Long> =
    productSearchRepository.countByCategory(keyword)
}
