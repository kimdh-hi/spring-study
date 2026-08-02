package com.study.springelasticsearch.product.domain.repository

import com.study.springelasticsearch.product.domain.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

  fun findByNameContainingOrDescriptionContaining(name: String, description: String): List<Product>
}
