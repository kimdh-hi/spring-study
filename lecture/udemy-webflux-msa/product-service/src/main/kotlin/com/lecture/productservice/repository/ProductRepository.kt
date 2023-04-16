package com.lecture.productservice.repository

import com.lecture.productservice.entity.Product
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ProductRepository: ReactiveCrudRepository<Product, String> {
}