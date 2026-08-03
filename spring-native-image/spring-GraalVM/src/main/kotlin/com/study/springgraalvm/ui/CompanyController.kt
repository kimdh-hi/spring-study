package com.study.springgraalvm.ui

import com.study.springgraalvm.application.CompanyService
import com.study.springgraalvm.domain.model.Company
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies")
class CompanyController(
  private val companyService: CompanyService,
) {
  @PostMapping
  fun create(@RequestBody request: CreateCompanyRequest): CompanyResponse =
    CompanyResponse.from(companyService.create(request.name))

  @GetMapping("/{id}")
  fun findById(@PathVariable id: Long): CompanyResponse =
    CompanyResponse.from(companyService.findById(id))

  @GetMapping
  fun findAll(): List<CompanyResponse> =
    companyService.findAll().map(CompanyResponse::from)
}

data class CreateCompanyRequest(val name: String)

data class CompanyResponse(val id: Long, val name: String, val userCount: Int) {
  companion object {
    fun from(company: Company) = CompanyResponse(company.id, company.name, company.users.size)
  }
}
