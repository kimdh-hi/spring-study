package com.toy.oauth2clientcredentials.ui.admin

import com.toy.oauth2clientcredentials.application.admin.AdminLoginService
import com.toy.oauth2clientcredentials.infra.security.admin.IssuedToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminLoginController(
  private val adminLoginService: AdminLoginService,
) {

  @PostMapping("/login")
  fun login(@RequestBody request: AdminLoginRequest): IssuedToken =
    adminLoginService.login(request.username, request.password)
}

data class AdminLoginRequest(
  val username: String,
  val password: String,
)
