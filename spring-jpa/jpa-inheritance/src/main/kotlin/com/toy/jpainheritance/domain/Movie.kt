package com.toy.jpainheritance.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(value = "M")
class Movie(
  var director: String,
  var actor: String,

  name: String,
  price: Int
): Item(name = name, price = price)