package com.toy.reactivejdsl.controller

import com.toy.reactivejdsl.common.SecurityUtils
import com.toy.reactivejdsl.service.UserService
import com.toy.reactivejdsl.vo.UserResponseVO
import com.toy.reactivejdsl.vo.UserSaveRequestVO
import com.toy.reactivejdsl.vo.UserSaveResponseVO
import com.toy.reactivejdsl.vo.UserSearchVO
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserController(
  private val userService: UserService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping
  suspend fun save(@RequestBody @Valid requestVO: UserSaveRequestVO): ResponseEntity<UserSaveResponseVO> {
    val jwtPrincipal = SecurityUtils.getPrincipal()

    log.info("jwtPrinipal: {}", jwtPrincipal)

    val responseVO = userService.save(requestVO)
    return ResponseEntity.ok(responseVO)
  }

  @GetMapping
  suspend fun search(requestVO: UserSearchVO, pageable: Pageable): ResponseEntity<Page<UserResponseVO>> {
    val page = userService.search(pageable, requestVO)
    return ResponseEntity.ok(page)
  }

  @GetMapping("/{id}")
  suspend fun get(@PathVariable id: String): ResponseEntity<UserResponseVO> {
    val user = userService.get(id)

    return ResponseEntity.ok(user)
  }

  @GetMapping("/v2/{id}")
  suspend fun getV2(@PathVariable id: String): ResponseEntity<UserResponseVO> {
    val user = userService.getV2(id)

    return ResponseEntity.ok(user)
  }
}