package com.toy.jpaeventlistener.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
@Table(name = "tb_user")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val username: String
) {
  override fun toString(): String {
    return "User(id=$id, username='$username')"
  }
}

interface UserRepository: JpaRepository<User, String>