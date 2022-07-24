package com.toy.reactivejdsl.service

import com.toy.reactivejdsl.domain.Company
import com.toy.reactivejdsl.repository.query.CompanyQuery
import com.toy.reactivejdsl.repository.command.CompanyRepository
import com.toy.reactivejdsl.vo.CompanySaveRequestVO
import com.toy.reactivejdsl.vo.CompanySaveResponseVO
import org.springframework.stereotype.Service

@Service
class CompanyService(
  private val companyRepository: CompanyRepository,
  private val companyQuery: CompanyQuery
) {

  suspend fun save(requestVO: CompanySaveRequestVO): CompanySaveResponseVO {
    val company = Company(name = requestVO.name)
    val savedCompany = companyRepository.save(company)
    return CompanySaveResponseVO.of(savedCompany)
  }
}