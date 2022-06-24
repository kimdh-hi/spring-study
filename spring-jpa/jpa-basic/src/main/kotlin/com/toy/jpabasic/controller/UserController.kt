package com.toy.jpabasic.controller

import com.toy.jpabasic.domain.User
import com.toy.jpabasic.repository.CompanyRepository
import com.toy.jpabasic.service.UserService
import com.toy.jpabasic.vo.UserSaveRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
  private val companyRepository: CompanyRepository,
  private val userService: UserService
) {

  @PostMapping
  fun save(@RequestBody requestVO: UserSaveRequestVO): ResponseEntity<User> {
    val company = companyRepository.findByIdOrNull("comp-01")!!
    val user = requestVO.toEntity(company)
    val savedUser = userService.save(user)

    return ResponseEntity.ok(savedUser)
  }

  @PostMapping("/authentication/{id}")
  fun auth(@PathVariable id: String) {
    userService.authenticationV2(id)
  }
}