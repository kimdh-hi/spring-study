package com.toy.dpopproofjwt.ui.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.toy.dpopproofjwt.application.auth.IssuedToken
import com.toy.dpopproofjwt.application.auth.LoginCommand
import com.toy.dpopproofjwt.application.auth.LoginService
import com.toy.dpopproofjwt.common.exception.InvalidDPoPProofException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private const val DPOP_HEADER = "DPoP"

@RestController
class LoginController(
  private val loginService: LoginService,
) {

  @PostMapping("/login")
  fun login(
    @RequestBody request: LoginRequest,
    httpRequest: HttpServletRequest,
  ): LoginResponse {
    val dPoPProof = httpRequest.getHeaders(DPOP_HEADER).toList().singleOrNull()
      ?: throw InvalidDPoPProofException("Exactly one DPoP header is required.")

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

// RFC 6749 5.1 token response parameter names
data class LoginResponse(
  @JsonProperty("access_token") val accessToken: String,
  @JsonProperty("token_type") val tokenType: String,
  @JsonProperty("expires_in") val expiresIn: Long,
) {
  constructor(token: IssuedToken) : this(token.value, token.type.value, token.expiresIn)
}
