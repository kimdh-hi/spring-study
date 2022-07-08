package com.toy.webfluxr2dbcpostgres.controller

import com.toy.webfluxr2dbcpostgres.common.Constants
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/auth")
@RestController
@SecurityRequirement(name = Constants.SWAGGER_SECURITY_SCHEME_KEY)
class AuthController {

  @GetMapping
  fun auth() = "ok"
}