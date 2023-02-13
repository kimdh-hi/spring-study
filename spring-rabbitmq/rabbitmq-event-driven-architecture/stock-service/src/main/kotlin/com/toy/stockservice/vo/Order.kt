package com.toy.stockservice.vo

import com.toy.stockservice.config.NoArg

@NoArg
data class Order(
  var orderId: String? = null,
  val name: String,
  val qty: Int,
  val price: Int
)