package com.toy.jpaeventlistener.domain

import com.toy.jpaeventlistener.domain.event.listener.UserJpaListener
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "tb_user")
@EntityListeners(UserJpaListener::class)
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