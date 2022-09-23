package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Company
import org.springframework.data.repository.CrudRepository

interface CompanyRepository: CrudRepository<Company, String>