package com.toy.jpabasic.controller

import com.toy.jpabasic.repository.CompanyRepository
import com.toy.jpabasic.service.CompanyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/companies")
class CompanyController(
  private val companyService: CompanyService
) {

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String) = ResponseEntity.ok(companyService.delete(id))
}