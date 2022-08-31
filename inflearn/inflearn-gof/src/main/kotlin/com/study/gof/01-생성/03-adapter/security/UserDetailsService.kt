package com.study.gof.`03-adapter`.security

interface UserDetailsService {

  fun loadUser(username: String): UserDetails
}