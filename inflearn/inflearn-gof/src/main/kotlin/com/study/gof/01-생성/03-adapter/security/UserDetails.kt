package com.study.gof.`03-adapter`.security

interface UserDetails {

  fun getUsername(): String

  fun getPassword(): String
}