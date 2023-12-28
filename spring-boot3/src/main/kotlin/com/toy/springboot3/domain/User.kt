package com.toy.springboot3.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Table
@Entity(name = "tb_user")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String?,

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type")
  var userType: UserType
)

enum class UserType {
  USER, ADMIN
}