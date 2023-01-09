package com.lecture.catalogservice.vo

import com.lecture.catalogservice.domain.Catalog
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime

data class CatalogResponseVO(
  val catalogId: String?,
  val productId: String,
  val price: Int,
  val stock: Int,
  val createdDate: LocalDateTime?
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -4821653560676976167L

    fun of(catalog: Catalog) = CatalogResponseVO(
      catalogId = catalog.id,
      productId = catalog.productId,
      price = catalog.price,
      stock = catalog.stock,
      createdDate = catalog.createdDate
    )
  }
}