package com.toy.springcoroutineexample.repository

import com.toy.springcoroutineexample.domain.Company
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CompanyRepository: CoroutineCrudRepository<Company, Long> {

  suspend fun findByName(name: String): Company?
}