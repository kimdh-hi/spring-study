package com.toy.oauthclientoidc.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ViewController {

  @GetMapping("/login")
  fun index(): String {
    return "login"
  }
}