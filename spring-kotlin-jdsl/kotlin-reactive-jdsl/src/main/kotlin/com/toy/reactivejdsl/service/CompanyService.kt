package com.toy.reactivejdsl.service

import com.toy.reactivejdsl.domain.Company
import com.toy.reactivejdsl.repository.CompanyRepository
import com.toy.reactivejdsl.vo.CompanySaveRequestVO
import com.toy.reactivejdsl.vo.CompanySaveResponseVO
import org.springframework.stereotype.Service

@Service
class CompanyService(
  private val companyRepository: CompanyRepository
) {

  suspend fun save(requestVO: CompanySaveRequestVO): CompanySaveResponseVO {
    val company = Company(name = requestVO.name)
    val savedCompany = companyRepository.save(company)
    return CompanySaveResponseVO.of(savedCompany)
  }
}