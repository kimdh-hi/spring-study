package com.toy.springjunitdemo.domain

import javax.persistence.*

@Entity
@Table(name = "tb_user")
class User(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var username: String
)