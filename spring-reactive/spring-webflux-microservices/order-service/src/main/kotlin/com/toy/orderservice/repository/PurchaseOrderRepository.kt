package com.toy.orderservice.repository

import com.toy.orderservice.domain.PurchaseOrder
import org.springframework.data.repository.CrudRepository

interface PurchaseOrderRepository: CrudRepository<PurchaseOrder, Int> {
}