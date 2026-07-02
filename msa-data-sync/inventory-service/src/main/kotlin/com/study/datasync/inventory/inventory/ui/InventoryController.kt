package com.study.datasync.inventory.inventory.ui

import com.study.datasync.inventory.inventory.domain.InventoryRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class InventoryResponse(
  val product: String,
  val quantity: Int,
)

@RestController
@RequestMapping("/api/inventory")
class InventoryController(
  private val inventoryRepository: InventoryRepository,
) {

  @GetMapping
  fun list(): List<InventoryResponse> =
    inventoryRepository.findAll().map { InventoryResponse(it.product, it.quantity) }
}
