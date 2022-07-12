package com.toy.reactivejdsl.vo

import com.toy.reactivejdsl.common.BaseVO
import com.toy.reactivejdsl.domain.Company
import java.io.Serial

data class CompanySaveRequestVO(
  val name: String
): BaseVO() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 8194617640280886363L
  }
}

data class CompanySaveResponseVO(
  val id: String,
  val name: String
): BaseVO() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 5556421702993277852L

    fun of(company: Company): CompanySaveResponseVO =
      CompanySaveResponseVO(id = company.id!!, name = company.name)
  }
}