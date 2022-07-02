package com.study.gof.composite.`1-before`

data class Bag (
  val items: MutableList<Item> = mutableListOf()
) {
  fun addItem(item: Item) {
    items.add(item)
  }
}