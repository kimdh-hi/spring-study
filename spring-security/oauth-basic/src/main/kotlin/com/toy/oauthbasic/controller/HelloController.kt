package com.toy.oauthbasic.controller

import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/hello")
  fun hello(@AuthenticationPrincipal oAuth2User: OAuth2User): OAuth2User {
    return oAuth2User
  }
}