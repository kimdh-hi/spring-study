package com.lecture.userservice.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @Column(nullable = false, unique = true)
  var email: String,

  @Column(nullable = false)
  var name: String,

  @Column(nullable = false)
  var password: String
): AbstractBaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -6205959036177763734L
  }
}