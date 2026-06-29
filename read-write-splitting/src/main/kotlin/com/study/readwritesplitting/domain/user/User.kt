package com.study.readwritesplitting.domain.user

import com.study.readwritesplitting.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "users")
class User private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 200, nullable = false)
  var name: String,
) : BaseEntity() {
  companion object {
    fun of(name: String) = User(name = name)
  }
}
