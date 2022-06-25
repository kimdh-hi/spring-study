package com.study.gof.adapter.security

class LoginHandler(
  private val userDetailsService: UserDetailsService
) {

  fun login(username: String, password: String): String {
    val userDetails = userDetailsService.loadUser(username)
    if (userDetails.getPassword() == password) {
      return userDetails.getUsername()
    } else {
      throw IllegalArgumentException("Failed to login ....")
    }
  }
}