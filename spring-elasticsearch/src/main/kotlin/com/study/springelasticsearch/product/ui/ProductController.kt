package com.study.springelasticsearch.product.ui

import com.study.springelasticsearch.product.application.ProductService
import com.study.springelasticsearch.product.domain.model.ProductSearchResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
  private val productService: ProductService,
) {

  @PostMapping
  fun register(@RequestBody request: ProductRegisterRequest): ProductResponse {
    val product = productService.register(request.name, request.description, request.category, request.price)
    return ProductResponse.from(product)
  }

  @GetMapping("/search")
  fun search(@RequestParam q: String, @RequestParam(defaultValue = "10") size: Int): List<ProductSearchResult> =
    productService.search(q, size)

  @GetMapping("/search/like")
  fun searchByLike(@RequestParam q: String): List<ProductResponse> =
    productService.searchByLike(q).map(ProductResponse::from)

  @GetMapping("/facet")
  fun facet(@RequestParam q: String): Map<String, Long> =
    productService.categoryFacet(q)
}
