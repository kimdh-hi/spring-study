package com.toy.consumer.domain

import com.toy.consumer.base.NoArg

@NoArg
data class Furniture(
  val color: String,
  val material: String,
  val name: String,
  val price: Int
)