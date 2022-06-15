package com.toy.springcoroutineexample.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "tb_company")
class Company (

  @Id
  var id: Long? = null,

  var name: String,
)