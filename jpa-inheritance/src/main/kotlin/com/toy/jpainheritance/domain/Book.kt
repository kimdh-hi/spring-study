package com.toy.jpainheritance.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(value = "B")
class Book(
  var author: String,
  var isbn: String,

  name: String,
  price: Int
): Item(name = name, price = price)