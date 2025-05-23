package com.toy.springboot3.domain

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UuidGenerator
import org.hibernate.type.SqlTypes

@Table
@Entity(name = "tb_user")
class User(
  @Id
  @UuidGenerator
  var id: String? = null,

  var name: String?,

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "user_type")
  var userType: UserType
)

enum class UserType {
  USER, ADMIN
}
