package com.toy.dpopproofjwt.ui.auth

import com.toy.dpopproofjwt.application.auth.IssuedToken
import com.toy.dpopproofjwt.application.auth.LoginCommand
import com.toy.dpopproofjwt.application.auth.LoginService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
  private val loginService: LoginService,
) {

  @PostMapping("/login")
  fun login(
    @RequestBody request: LoginRequest,
    @RequestHeader("DPoP") dPoPProof: String,
    httpRequest: HttpServletRequest,
  ): LoginResponse {
    val token = loginService.login(
      LoginCommand(
        username = request.username,
        password = request.password,
        dPoPProof = dPoPProof,
        method = httpRequest.method,
        targetUri = httpRequest.requestURL.toString(),
      ),
    )

    return LoginResponse(token)
  }
}

data class LoginRequest(
  val username: String,
  val password: String,
)

data class LoginResponse(
  val accessToken: String,
  val tokenType: String,
  val expiresIn: Long,
) {
  constructor(token: IssuedToken) : this(token.value, token.type.value, token.expiresIn)
}
