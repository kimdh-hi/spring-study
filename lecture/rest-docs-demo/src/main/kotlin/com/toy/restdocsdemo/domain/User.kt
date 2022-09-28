package com.toy.restdocsdemo.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
  var email: String,
  var name: String,
  var createdAt: LocalDateTime? = null,
  var updatedAt: LocalDateTime? = null
) {

  fun update(name: String?) {
    name?.let { this.name = it }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as User

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }

  override fun toString(): String {
    return "User(id=$id, email='$email', name='$name', createdAt=$createdAt, updatedAt=$updatedAt)"
  }
}