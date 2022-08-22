package com.toy.oauthbasic.controller

import com.toy.oauthbasic.oauth2.JwtPrincipal
import com.toy.oauthbasic.utils.SecurityUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

  @GetMapping
  fun auth(): JwtPrincipal {
    return SecurityUtils.getPrincipal()
  }
}