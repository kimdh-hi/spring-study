package com.study.springgraalvm.infra

import com.study.springgraalvm.domain.model.Company
import com.study.springgraalvm.domain.repository.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CompanyRepositoryImpl(
  private val jpa: CompanyJpaRepository,
) : CompanyRepository {
  override fun save(company: Company): Company = jpa.save(company)
  override fun findById(id: Long): Company? = jpa.findByIdOrNull(id)
  override fun findAll(): List<Company> = jpa.findAll()
}
