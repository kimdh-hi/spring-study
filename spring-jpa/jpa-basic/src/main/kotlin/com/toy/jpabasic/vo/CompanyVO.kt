package com.toy.jpabasic.vo

import com.toy.jpabasic.domain.Company
import com.toy.jpabasic.domain.User
import com.toy.jpabasic.repository.CompanyRepository

data class CompanyResponseVO (
  val id: String,
  val name: String
) {
  companion object {
    fun of(company: Company) = CompanyResponseVO(company.id!!, company.name)
  }
}