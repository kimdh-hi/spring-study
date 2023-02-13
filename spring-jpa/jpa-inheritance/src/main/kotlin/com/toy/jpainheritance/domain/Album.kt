package com.toy.jpainheritance.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(value = "A")
class Album(
  var artist: String,

  name: String,
  price: Int
): Item(name = name, price = price)