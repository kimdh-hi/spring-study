package com.toy.jpainheritance.domain

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn
abstract class Item(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = -1L,

  var name: String,

  var price: Int = 0,
)