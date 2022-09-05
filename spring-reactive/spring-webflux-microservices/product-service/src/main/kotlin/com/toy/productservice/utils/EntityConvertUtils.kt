package com.toy.productservice.utils

import com.toy.productservice.domain.Product
import com.toy.productservice.dto.ProductDto

object EntityConvertUtils {

  fun toDto(product: Product): ProductDto {
    return ProductDto(
      id = product.id,
      description = product.description,
      price = product.price
    )
  }

  fun toEntity(productDto: ProductDto): Product {
    return Product(
      id = productDto.id,
      description = productDto.description,
      price = productDto.price
    )
  }
}