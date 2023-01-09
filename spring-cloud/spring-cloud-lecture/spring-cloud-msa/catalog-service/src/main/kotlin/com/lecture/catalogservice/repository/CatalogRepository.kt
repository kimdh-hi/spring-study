package com.lecture.catalogservice.repository

import com.lecture.catalogservice.domain.Catalog
import org.springframework.data.jpa.repository.JpaRepository

interface CatalogRepository: JpaRepository<Catalog, String> {

  fun findByProductId(productId: String): Catalog?
}