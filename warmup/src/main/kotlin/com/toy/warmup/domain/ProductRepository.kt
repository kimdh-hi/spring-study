package com.toy.warmup.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

  fun findTop20ByCategoryOrderByPriceDesc(category: String): List<Product>
}
