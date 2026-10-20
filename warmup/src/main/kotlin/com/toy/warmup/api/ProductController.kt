package com.toy.warmup.api

import com.toy.warmup.domain.Product
import com.toy.warmup.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/products")
class ProductController(
  private val productService: ProductService,
) {

  @GetMapping
  fun list(@RequestParam category: String): List<ProductResponse> =
    productService.findTopByCategory(category).map(::ProductResponse)

  @GetMapping("/{id}")
  fun detail(@PathVariable id: Long): ResponseEntity<ProductResponse> {
    val product = productService.findById(id) ?: return ResponseEntity.notFound().build()
    return ResponseEntity.ok(ProductResponse(product))
  }

  @PostMapping
  fun create(@RequestBody request: ProductRequest): ResponseEntity<ProductResponse> {
    val product = productService.create(request.name, request.category, request.price)
    return ResponseEntity.created(URI("/products/${product.id}")).body(ProductResponse(product))
  }

  @PutMapping("/{id}")
  fun update(@PathVariable id: Long, @RequestBody request: ProductRequest): ResponseEntity<ProductResponse> {
    val product = productService.update(id, request.name, request.category, request.price)
      ?: return ResponseEntity.notFound().build()
    return ResponseEntity.ok(ProductResponse(product))
  }

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: Long): ResponseEntity<Unit> =
    if (productService.delete(id)) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()
}

data class ProductRequest(
  val name: String,
  val category: String,
  val price: Long,
)

data class ProductResponse(
  val id: Long,
  val name: String,
  val category: String,
  val price: Long,
) {
  constructor(product: Product) : this(product.id, product.name, product.category, product.price)
}
