package com.lecture.repository

import com.lecture.entity.PurchaseOrder
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseOrderRepository: JpaRepository<PurchaseOrder, Int> {
}