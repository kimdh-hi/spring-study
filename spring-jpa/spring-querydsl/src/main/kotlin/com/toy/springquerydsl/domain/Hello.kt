package com.toy.springquerydsl.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_hello")
class Hello (
  @Id @GeneratedValue
  var id: String? = null,
  val data: String,
  val data2: String
)