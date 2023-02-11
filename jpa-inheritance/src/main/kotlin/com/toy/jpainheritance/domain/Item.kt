package com.toy.jpainheritance.domain

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
class Item(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = -1L,

  var name: String,

  var price: Int = 0,
)