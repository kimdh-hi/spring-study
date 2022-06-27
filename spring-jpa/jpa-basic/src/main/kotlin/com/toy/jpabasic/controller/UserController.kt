package com.toy.jpabasic.controller

import com.toy.jpabasic.domain.User
import com.toy.jpabasic.repository.CompanyRepository
import com.toy.jpabasic.service.UserService
import com.toy.jpabasic.vo.CompanyResponseVO
import com.toy.jpabasic.vo.UserResponseVO
import com.toy.jpabasic.vo.UserSaveRequestVO
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@RestController
@RequestMapping("/api/users")
class UserController(
  private val companyRepository: CompanyRepository,
  private val userService: UserService,
) {

  @PersistenceContext
  lateinit var em: EntityManager
  private val log = LoggerFactory.getLogger(javaClass)

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

  @GetMapping("/{id}")
  fun get(@PathVariable id: String): ResponseEntity<UserResponseVO> {
    val user = userService.get(id)
    log.info("company-class: ${user.company::class.java}")

    return ResponseEntity.ok(UserResponseVO.of(user))
  }

  @GetMapping("/{id}/company")
  fun getCompany(@PathVariable id: String): ResponseEntity<CompanyResponseVO> {
    val user = userService.get(id)
    val result = em.contains(user)
    log.info("em : {}", result)
    return ResponseEntity.ok(CompanyResponseVO.of(user.company))
  }
}