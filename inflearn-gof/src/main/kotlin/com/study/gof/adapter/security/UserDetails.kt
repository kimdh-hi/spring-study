package com.study.gof.adapter.security

interface UserDetails {

  fun getUsername(): String

  fun getPassword(): String
}