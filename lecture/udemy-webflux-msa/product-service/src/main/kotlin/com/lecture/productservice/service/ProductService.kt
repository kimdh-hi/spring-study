package com.lecture.productservice.service

import com.lecture.productservice.dto.ProductDto
import com.lecture.productservice.repository.ProductRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
  private val productRepository: ProductRepository
) {

  fun getAll(): Flux<ProductDto> = productRepository.findAll()
    .map { ProductDto.of(it) }

  fun get(id: String): Mono<ProductDto> = productRepository.findById(id)
    .map { ProductDto.of(it) }

  fun save(dto: Mono<ProductDto>): Mono<ProductDto> = dto
    .map { it.toEntity() }
    .flatMap { productRepository.save(it) }
    .map { ProductDto.of(it) }

  fun update(id: String, dto: ProductDto): Mono<ProductDto> = productRepository.findById(id)
    .map {
      it.update(dto.description, dto.price)
      it
    }
    .flatMap { productRepository.save(it) }
    .map { ProductDto.of(it) }

  fun delete(id: String): Mono<Void> = productRepository.deleteById(id)
}