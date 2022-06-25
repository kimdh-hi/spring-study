package com.study.gof.adapter.security

interface UserDetailsService {

  fun loadUser(username: String): UserDetails
}