package com.toy.springflyway.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tb_user")
class User(
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  var id: String? = null,

  @Column(nullable = false, unique = true)
  var name: String,

  var age: Int
)
