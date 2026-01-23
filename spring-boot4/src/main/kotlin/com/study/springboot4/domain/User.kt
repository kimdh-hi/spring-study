package com.study.springboot4.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator

@Entity
class User(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 100, nullable = false)
  var name: String,
)



