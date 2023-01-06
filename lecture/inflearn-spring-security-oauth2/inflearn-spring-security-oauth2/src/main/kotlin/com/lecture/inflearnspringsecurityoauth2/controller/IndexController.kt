package com.lecture.inflearnspringsecurityoauth2.controller

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Controller
class IndexController(
  private val clientRegistrationRepository: ClientRegistrationRepository
) {

  @GetMapping("/")
  fun index(): String {
    return "index"
  }
}