package com.toy.orderservice.vo

data class Order(
  var orderId: String? = null,
  val name: String,
  val qty: Int,
  val price: Int
)