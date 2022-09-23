package com.toy.jpabasic.service

import com.toy.jpabasic.repository.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompanyService(
  private val companyRepository: CompanyRepository
) {

  @Transactional
  fun delete(id: String) {
    companyRepository.findByIdOrNull(id)?.let {
      companyRepository.delete(it)
    }
  }
}