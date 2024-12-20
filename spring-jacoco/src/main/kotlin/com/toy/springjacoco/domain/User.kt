package com.toy.springjacoco.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "jacoco_user")
class User(
  @Id
  @UuidGenerator
  @Column(length = 100)
  var id: String? = null,

  @Column(length = 100, unique = true, nullable = false)
  var name: String,
)

