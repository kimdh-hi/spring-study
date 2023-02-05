package com.lecture.oauth2resourceserver.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

  @GetMapping
  fun index(authentication: Authentication) = authentication
}