package com.toy.productservice.repository

import com.toy.productservice.domain.Product
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: ReactiveMongoRepository<Product, String> {
}