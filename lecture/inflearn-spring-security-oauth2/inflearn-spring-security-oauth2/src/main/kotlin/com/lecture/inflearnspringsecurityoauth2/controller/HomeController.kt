package com.lecture.inflearnspringsecurityoauth2.controller

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/api/users")
  fun user(authentication: Authentication, @AuthenticationPrincipal oAuth2User: OAuth2User): String {
    log.info("authentication: {}, oAuth2User: {}", authentication, oAuth2User)
    return "auth"
  }

  @GetMapping("/api/oidc")
  fun oidc(authentication: Authentication, @AuthenticationPrincipal oidcUser: OidcUser): String {
    log.info("authentication: {}, oidcUser: {}", authentication, oidcUser)
    return "auth"
  }
}