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

  fun getAll(): Flux<ProductDto> = productRepository.findAll()
      .map { EntityConvertUtils.toDto(it) }

  fun getById(id: String): Mono<ProductDto> = productRepository.findById(id)
      .map { EntityConvertUtils.toDto(it) }

  fun save(productDtoMono: Mono<ProductDto>): Mono<ProductDto> = productDtoMono
      .map { EntityConvertUtils.toEntity(it) }
      .flatMap { productRepository.insert(it) }
      .map { EntityConvertUtils.toDto(it) }

  fun update(id: String, productDtoMono: Mono<ProductDto>) = productRepository.findById(id)
    .flatMap { productDtoMono
      .map {EntityConvertUtils.toEntity(it)}
      .doOnNext { it.id = id }
    }
    .flatMap { productRepository.save(it) }
    .map { EntityConvertUtils.toDto(it)}

  fun delete(id: String) = productRepository.deleteById(id)
}