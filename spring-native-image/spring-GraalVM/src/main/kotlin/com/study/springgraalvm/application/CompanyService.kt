package com.study.springgraalvm.application

import com.study.springgraalvm.domain.model.Company
import com.study.springgraalvm.domain.repository.CompanyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CompanyService(
  private val companyRepository: CompanyRepository,
) {
  fun create(name: String): Company = companyRepository.save(Company(name = name))

  @Transactional(readOnly = true)
  fun findById(id: Long): Company =
    (companyRepository.findById(id) ?: throw NoSuchElementException("company not found: $id"))
      .also { it.users.size }

  @Transactional(readOnly = true)
  fun findAll(): List<Company> =
    companyRepository.findAll().onEach { it.users.size }
}
