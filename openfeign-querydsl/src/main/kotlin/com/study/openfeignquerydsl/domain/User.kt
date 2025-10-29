package com.study.openfeignquerydsl.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator

@Entity
class User private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 50, nullable = false)
  val name: String,

  @Column(length = 50, nullable = false)
  val email: Email,

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  val team: Team
) {
  companion object {
    fun of(name: String, email: Email, team: Team) = User(name = name, email = email, team = team)
  }
}
