package com.study.jpacore.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator

@Entity
class UserData(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(nullable = false, length = 100)
  var data: String,

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  val user: User,
)
