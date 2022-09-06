package com.toy.productservice.controller

import com.toy.productservice.dto.ProductDto
import com.toy.productservice.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/products")
class ProductController(
  private val productService: ProductService
) {

  @GetMapping
  fun getAll() = productService.getAll()

  @GetMapping("/price-range")
  fun searchByPriceRange(@RequestParam min: String, @RequestParam max: String) =
    productService.searchByPriceRange(min.toInt(), max.toInt())

  @GetMapping("/{id}")
  fun get(@PathVariable id: String) = productService.getById(id)
    .map { ResponseEntity.ok(it) }
    .defaultIfEmpty(ResponseEntity.notFound().build())

  @PostMapping
  fun save(@RequestBody requestVO: Mono<ProductDto>) = productService.save(requestVO)
    .map { ResponseEntity.ok(it) }

  @PutMapping("/{id}")
  fun update(
    @PathVariable id: String,
    @RequestBody requestVO: Mono<ProductDto>) = productService.update(id, requestVO)
    .map { ResponseEntity.ok(it) }
    .defaultIfEmpty(ResponseEntity.notFound().build())

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String) = productService.delete(id)
    .map { ResponseEntity.noContent() }
}