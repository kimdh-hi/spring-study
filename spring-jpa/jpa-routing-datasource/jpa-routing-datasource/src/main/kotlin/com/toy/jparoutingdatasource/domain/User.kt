package com.toy.jparoutingdatasource.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_user")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var username: String,
)