package com.toy.dpopproofjwt.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Item(
  var name: String,
  @Id @GeneratedValue
  val id: Long? = null,
)
