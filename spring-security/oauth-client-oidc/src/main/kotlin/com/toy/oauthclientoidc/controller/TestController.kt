package com.toy.oauthclientoidc.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

  @GetMapping("/oidc-principal")
  fun test(@AuthenticationPrincipal oidcUser: OidcUser): OidcUser {
    return oidcUser
  }
}