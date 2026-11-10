package com.toy.dpopproofjwt.ui.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.toy.dpopproofjwt.application.auth.DPoPProof
import com.toy.dpopproofjwt.application.auth.IssuedToken
import com.toy.dpopproofjwt.application.auth.LoginCommand
import com.toy.dpopproofjwt.application.auth.LoginService
import com.toy.dpopproofjwt.application.auth.RefreshCommand
import com.toy.dpopproofjwt.application.auth.RefreshTokenService
import com.toy.dpopproofjwt.common.exception.InvalidDPoPProofException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private const val DPOP_HEADER = "DPoP"

@RestController
class LoginController(
  private val loginService: LoginService,
  private val refreshTokenService: RefreshTokenService,
) {

  @PostMapping("/login")
  fun login(
    @RequestBody request: LoginRequest,
    httpRequest: HttpServletRequest,
  ): TokenResponse {
    val token = loginService.login(
      LoginCommand(
        username = request.username,
        password = request.password,
        proof = httpRequest.dPoPProof(),
      ),
    )

    return TokenResponse(token)
  }

  @PostMapping("/token/refresh")
  fun refresh(
    @RequestBody request: RefreshRequest,
    httpRequest: HttpServletRequest,
  ): TokenResponse {
    val token = refreshTokenService.refresh(
      RefreshCommand(
        refreshToken = request.refreshToken,
        proof = httpRequest.dPoPProof(),
      ),
    )

    return TokenResponse(token)
  }

  private fun HttpServletRequest.dPoPProof(): DPoPProof {
    val value = getHeaders(DPOP_HEADER).toList().singleOrNull()
      ?: throw InvalidDPoPProofException("Exactly one DPoP header is required.")
    return DPoPProof(value, method, requestURL.toString())
  }
}

data class LoginRequest(
  val username: String,
  val password: String,
)

data class RefreshRequest(
  @JsonProperty("refresh_token") val refreshToken: String,
)

data class TokenResponse(
  @JsonProperty("access_token") val accessToken: String,
  @JsonProperty("token_type") val tokenType: String,
  @JsonProperty("expires_in") val expiresIn: Long,
  @JsonProperty("refresh_token") val refreshToken: String,
) {
  constructor(token: IssuedToken) : this(
    token.accessToken.value,
    token.accessToken.type.value,
    token.accessToken.expiresIn,
    token.refreshToken,
  )
}
