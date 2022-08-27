package com.toy.emailservice.vo

import com.toy.emailservice.config.NoArg

@NoArg
data class Order(
  var orderId: String? = null,
  val name: String,
  val qty: Int,
  val price: Int
)