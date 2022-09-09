package com.toy.orderservice.domain

import com.toy.orderservice.dto.OrderStatus
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "tb_purchase_order")
data class PurchaseOrder(
  @Id
  @GeneratedValue
  var id: Int? = null,
  val productId: Int,
  val userId: Int,
  val amount: Int,
  val status: OrderStatus
)