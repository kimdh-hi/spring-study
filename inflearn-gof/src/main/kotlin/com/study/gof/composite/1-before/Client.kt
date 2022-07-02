package com.study.gof.composite.`1-before`

fun main() {
  val itemA = Item("itemA", 100)
  val itemB = Item("itemB", 500)

  val bag = Bag()
  bag.addItem(itemA)
  bag.addItem(itemB)

  priceItem(itemA)
  priceBag(bag)
}

/**
 * 아래 코드가 클라이언트 측에 있어야 할 코드인가?
 */

fun priceItem(item: Item) {
  println("item price: ${item.price}")
}

fun priceBag(bag: Bag) {
  println("bag price: ${bag.items.map { it.price }.sum()}")
}
