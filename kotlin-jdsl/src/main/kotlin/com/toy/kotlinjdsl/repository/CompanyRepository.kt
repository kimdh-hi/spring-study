package com.toy.kotlinjdsl.repository

import com.toy.kotlinjdsl.domain.Company
import org.springframework.data.repository.CrudRepository

interface CompanyRepository: CrudRepository<Company, String>