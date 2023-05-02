package com.lecture.entity

import com.lecture.dto.OrderStatus
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class PurchaseOrder(
  @Id
  @GeneratedValue
  var id: Int = 0,
  val productId: String,
  val userId: Int,
  var amount: Int,
  var status: OrderStatus
)