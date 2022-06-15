package com.toy.springcoroutineexample.service

import com.toy.springcoroutineexample.repository.CompanyRepository
import org.springframework.stereotype.Service

@Service
class CompanyService(private val companyRepository: CompanyRepository) {

}