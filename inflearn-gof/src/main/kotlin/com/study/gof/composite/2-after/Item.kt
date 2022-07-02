package com.study.gof.composite.`2-after`

data class Item (
  var name: String,
  var price: Int
): Component {

  override fun getPrice(): Int = this.price
}