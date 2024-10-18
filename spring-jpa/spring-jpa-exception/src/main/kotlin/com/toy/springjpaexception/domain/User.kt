package com.toy.springjpaexception.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "tb_user")
class User(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 100, nullable = false)
  var username: String
) {
  companion object {
    fun of(username: String) = User(
      username = username
    )
  }
}

interface UserRepository : JpaRepository<User, String>
