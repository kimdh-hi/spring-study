package com.toy.springkotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.repository.JpaRepository

@Entity
class JustNonNullIdEntity private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String = "",

  @Column(length = 100, nullable = false)
  var data: String
) {
  companion object {
    fun of(data: String): JustNonNullIdEntity {
      return JustNonNullIdEntity(data = data)
    }
  }
}

interface JustNonNullIdEntityRepository : JpaRepository<JustNonNullIdEntity, String>
