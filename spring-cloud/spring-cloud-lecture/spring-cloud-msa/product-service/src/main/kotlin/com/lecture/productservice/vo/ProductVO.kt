package com.lecture.productservice.vo

import com.lecture.productservice.domain.Product
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime

data class ProductResponseVO(
  val productId: String?,
  val price: Int,
  val stock: Int,
  val createdDate: LocalDateTime?
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -4821653560676976167L

    fun of(product: Product) = ProductResponseVO(
      productId = product.id,
      price = product.price,
      stock = product.stock,
      createdDate = product.createdDate
    )
  }
}