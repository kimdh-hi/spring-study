package com.study.springgraalvm.domain.repository

import com.study.springgraalvm.domain.model.Company

interface CompanyRepository {
  fun save(company: Company): Company
  fun findById(id: Long): Company?
  fun findAll(): List<Company>
}
