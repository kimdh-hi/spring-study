package com.example.ex.controller

import com.example.ex.exception.ParameterException
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RequestMapping("/enum")
@RestController
class EnumMappingTestController {

  @PostMapping
  fun post(
    @RequestBody @Valid requestVO: EnumRequestVO, result: BindingResult
  ): ResponseEntity<EnumRequestVO> {
    if(result.hasErrors()) {
      throw ParameterException(result)
    }

    return ResponseEntity.ok(requestVO)
  }
}

data class EnumRequestVO (
  @field:NotNull
  val username: String,

  @field:NotNull
  val roleCode: RoleCode
)

enum class RoleCode(
  @get:JsonValue val id: String,
  val roleName: String) {
  ROLE_USER("role-01", "user"),
  ROLE_ADMIN("role-09", "admin");

  companion object {
    // RoleCode 타입으로 역직렬화 될 때 동작 지정
    // default는 name에 해당하는 "ROLE_USER" 와 매핑
    // id 에 매핑되어 역직렬화되도록 하기 위함
    @JsonCreator
    fun creator(id: String): RoleCode
      = values().firstOrNull { it.id == id } ?: throw RuntimeException("not exists roleId...")
  }
}