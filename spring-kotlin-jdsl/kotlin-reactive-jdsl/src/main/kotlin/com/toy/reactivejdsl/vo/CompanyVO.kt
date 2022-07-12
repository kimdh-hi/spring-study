package com.toy.reactivejdsl.vo

import com.toy.reactivejdsl.domain.Company

data class CompanySaveRequestVO(
  val name: String
)

data class CompanySaveResponseVO(
  val id: String,
  val name: String
) {
  companion object {
    fun of(company: Company): CompanySaveResponseVO =
      CompanySaveResponseVO(id = company.id!!, name = company.name)
  }
}