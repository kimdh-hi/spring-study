package com.toy.jpabulkinsert.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "teams")
class Team(
  @Id
  @UuidGenerator
  var id: String? = null,

  @Column(nullable = false, length = 100)
  var name: String,
)
