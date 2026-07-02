package com.study.datasync.inventory.inventory.domain

import org.springframework.data.jpa.repository.JpaRepository

interface InventoryRepository : JpaRepository<Inventory, String> {
  fun findByProduct(product: String): Inventory?
}
