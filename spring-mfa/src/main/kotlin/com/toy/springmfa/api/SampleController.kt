package com.toy.springmfa.api

import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.FactorGrantedAuthority
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {

  @GetMapping("/admin/me")
  fun admin(authentication: Authentication) = describe(authentication)

  private fun describe(authentication: Authentication) = mapOf(
    "username" to authentication.name,
    "factors" to authentication.authorities
      .filterIsInstance<FactorGrantedAuthority>()
      .associate { it.authority to it.issuedAt },
  )
}
