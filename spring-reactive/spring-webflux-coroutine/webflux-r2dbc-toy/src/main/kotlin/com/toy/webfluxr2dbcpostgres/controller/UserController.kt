package com.toy.webfluxr2dbcpostgres.controller

import com.toy.webfluxr2dbcpostgres.common.Constants
import com.toy.webfluxr2dbcpostgres.domain.User
import com.toy.webfluxr2dbcpostgres.service.UserService
import com.toy.webfluxr2dbcpostgres.vo.UserUpdateRequestVO
import com.toy.webfluxr2dbcpostgres.vo.UserSaveRequestVO
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RequestMapping("/api/users")
@RestController
@Tag(name = "UserController-Tag")
@SecurityRequirement(name = Constants.SWAGGER_SECURITY_SCHEME_KEY)
class UserController(
  private val userService: UserService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping("/signup")
  suspend fun signup(@RequestBody @Valid requestVO: UserSaveRequestVO) = userService.save(requestVO)

  @GetMapping
  fun list(): ResponseEntity<Flow<User>> {
    val list = userService.list()
    Thread.sleep(1000L)

    return ResponseEntity.ok(list)
  }

  @GetMapping("/{id}")
  suspend fun get(@PathVariable id: Long): ResponseEntity<User> {

    val user = userService.get(id)
    return ResponseEntity.ok(user)
  }

  @PutMapping("/{id}")
  suspend fun update(@PathVariable id: Long, @RequestBody requestVO: UserUpdateRequestVO): ResponseEntity<Unit> {
    userService.update(id, requestVO)
    return ResponseEntity.noContent().build()
  }

  @DeleteMapping("/{id}")
  suspend fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
    userService.delete(id)
    return ResponseEntity.noContent().build()
  }
}