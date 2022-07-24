package com.toy.reactivejdsl.base

import com.toy.reactivejdsl.domain.Authority
import com.toy.reactivejdsl.domain.Company
import com.toy.reactivejdsl.domain.Role
import com.toy.reactivejdsl.domain.User

object TestData {

  val COMPANY = Company(id = "comp-01", name = "comp01")
  val ROLE_ADMIN = Role(id = "role-09", name = "admin", authorities = mutableSetOf(Authority.ADMIN, Authority.USER))
  val ROLE_USER = Role(id = "role-01", name = "user", authorities = mutableSetOf(Authority.USER))
  val USER = User(id = "user-01", "kim", "kim@gmail.com", "{noop}1234", company = COMPANY, role = ROLE_ADMIN)
}