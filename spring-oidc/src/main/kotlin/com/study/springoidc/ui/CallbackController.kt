package com.study.springoidc.ui

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CallbackController {

  @GetMapping("/authorized")
  fun authorized(@RequestParam code: String, @RequestParam(required = false) state: String?): Map<String, String?> =
    mapOf("code" to code, "state" to state)
}
