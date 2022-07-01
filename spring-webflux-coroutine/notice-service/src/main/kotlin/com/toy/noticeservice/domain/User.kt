package com.toy.noticeservice.domain

import java.io.Serial
import javax.persistence.Table

@Table(name = "tb_user")
class User (
  var id: String,

  var name: String,
  var username: String,
  var password: String,
  var role: Role
): AbstractTraceByEntity() {

  companion object {
    @Serial
    private const val serialVersionUID: Long = 759903295155059261L
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as User

    if (username != other.username) return false

    return true
  }

  override fun hashCode(): Int {
    return username.hashCode()
  }

  override fun toString(): String {
    return "User(id='$id', name='$name', username='$username', password='$password', role=$role)"
  }

}