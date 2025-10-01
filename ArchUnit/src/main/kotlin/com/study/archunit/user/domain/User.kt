package com.study.archunit.user.domain

import com.study.archunit.user.domain.enums.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator

@Entity
class User private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 200, nullable = false)
  var name: String,

//  @Enumerated(EnumType.STRING)
  @Column(length = 10, nullable = false)
  var type: UserType
) : BaseEntity() {
  companion object {
    fun of(name: String, type: UserType) = User(
      name = name,
      type = type,
    )
  }
}
