package com.study.springoidc.ui

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.annotation.AuthenticationPrincipal

@RestController
@RequestMapping("/api")
class MeController {

  @GetMapping("/me")
  fun me(@AuthenticationPrincipal jwt: Jwt): Map<String, Any?> = mapOf(
    "subject" to jwt.subject,
    "issuer" to jwt.issuer.toString(),
    "scopes" to jwt.getClaimAsStringList("scope"),
    "roles" to jwt.getClaimAsStringList("roles"),
    "expiresAt" to jwt.expiresAt,
  )

  @GetMapping("/scoped/resource")
  fun scopedResource(@AuthenticationPrincipal jwt: Jwt): Map<String, Any?> = mapOf(
    "message" to "read scope required",
    "audience" to jwt.audience,
    "subject" to jwt.subject,
  )
}
