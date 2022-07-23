package com.toy.emailauthentication.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_user")
class User (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,
  var username: String,
  var password: String,
  var emailAuthenticated: Boolean = false
) {
  companion object {
    fun newInstance(username: String, password: String): User {
      return User(username = username, password = password, emailAuthenticated = false)
    }
  }

  fun authenticate() {
    this.emailAuthenticated = true
  }
}