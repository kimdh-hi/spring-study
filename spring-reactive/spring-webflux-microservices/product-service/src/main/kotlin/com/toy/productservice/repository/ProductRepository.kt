package com.toy.productservice.repository

import com.toy.productservice.domain.Product
import org.springframework.data.domain.Range
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface ProductRepository: ReactiveMongoRepository<Product, String> {

//  fun findByPriceBetween(min: Int, max: Int): Flux<Product>
  fun findByPriceBetween(range: Range<Int>): Flux<Product>
}