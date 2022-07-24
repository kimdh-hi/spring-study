package com.toy.basic.`03-default_named_arguments`

fun main() {
  val enabledUser = User(username = "user1@rsup.com", password = "pass1234")
  val enabledUser2 = User(id = "user-02", username = "user2@rsup.com", password = "pass1234")
  val disabledUser
    = User(username = "user2rsup.com", password = "1234pass", status = UserStatus.DISABLED)
}

class User(
  var id: String? = null,
  var username: String,
  var password: String,
  var status: UserStatus = UserStatus.ENALBED,
)

enum class UserStatus {
  ENALBED, DISABLED
}

