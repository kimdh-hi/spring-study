package com.toy.springr2dbc.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.relational.core.mapping.Table

//@Table 어노테이션 경로 주의 jakarta.persistence.Table 아님
@Table
@Entity(name = "tb_user")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String = "",

  var name: String
)