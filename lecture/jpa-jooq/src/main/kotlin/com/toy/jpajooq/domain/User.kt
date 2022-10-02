package com.toy.jpajooq.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_user")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(length = 50)
  var id: String? = null,

  var username: String,

  var password: String,
)