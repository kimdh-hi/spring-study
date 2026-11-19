package com.toy.oauth2clientcredentials.ui.api

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ServiceApiController {

  @GetMapping("/service-a")
  fun serviceA(@AuthenticationPrincipal jwt: Jwt) = ApiResponse(jwt.subject!!, "service-a")

  @GetMapping("/service-b")
  fun serviceB(@AuthenticationPrincipal jwt: Jwt) = ApiResponse(jwt.subject!!, "service-b")
}

data class ApiResponse(
  val clientId: String,
  val data: String,
)
