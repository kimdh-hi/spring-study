package com.toy.springquerydsl.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tb_hello")
class Hello (
  @Id @GeneratedValue
  var id: String? = null,
  val data: String,
  val data2: String
)
