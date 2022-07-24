package com.toy.reactivejdsl.controller

import com.toy.reactivejdsl.service.CompanyService
import com.toy.reactivejdsl.vo.CompanySaveRequestVO
import com.toy.reactivejdsl.vo.CompanySaveResponseVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/companies")
class CompanyController(
  private val companyService: CompanyService
) {

  @PostMapping
  suspend fun save(@RequestBody requestVO: CompanySaveRequestVO): ResponseEntity<CompanySaveResponseVO> {
    val responseVO = companyService.save(requestVO)
    return ResponseEntity.ok(responseVO)
  }
}