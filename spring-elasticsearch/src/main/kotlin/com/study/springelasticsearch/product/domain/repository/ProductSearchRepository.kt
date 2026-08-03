package com.study.springelasticsearch.product.domain.repository

import com.study.springelasticsearch.product.domain.model.Product
import com.study.springelasticsearch.product.domain.model.ProductSearchResult

interface ProductSearchRepository {

  fun index(product: Product)

  fun search(keyword: String, size: Int): List<ProductSearchResult>

  fun countByCategory(keyword: String): Map<String, Long>
}
