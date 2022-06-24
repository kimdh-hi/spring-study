package com.toy.jpabasic.vo

import com.toy.jpabasic.domain.Company
import com.toy.jpabasic.domain.User

data class UserSaveRequestVO(
  val username: String
) {

  fun toEntity(company: Company) = User(username = username, company = company)
}