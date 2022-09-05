package com.toy.productservice.service

import com.toy.productservice.domain.Product
import com.toy.productservice.dto.ProductDto
import com.toy.productservice.repository.ProductRepository
import com.toy.productservice.utils.EntityConvertUtils
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
  private val productRepository: ProductRepository
) {

  fun getAll(): Flux<ProductDto> {
    return productRepository.findAll()
      .map { EntityConvertUtils.toDto(it) }
  }

  fun getById(id: String): Mono<ProductDto> {
    return productRepository.findById(id)
      .map { EntityConvertUtils.toDto(it) }
  }

  fun save(productDtoMono: Mono<ProductDto>): Mono<ProductDto> {
    return productDtoMono
      .map { EntityConvertUtils.toEntity(it) }
      .flatMap { productRepository.insert(it) }
      .map { EntityConvertUtils.toDto(it) }
  }

}