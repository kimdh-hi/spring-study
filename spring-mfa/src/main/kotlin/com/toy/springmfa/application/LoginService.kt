package com.toy.springmfa.application

import org.springframework.stereotype.Service

@Service
class LoginService(
  private val factorTokenService: FactorTokenService,
  private val passkeyService: PasskeyService,
) {

  fun login(username: String, password: String): LoginResult {
    val pendingToken = factorTokenService.login(username, password)
    val nextStep = if (passkeyService.hasPasskey(username)) {
      NextStep.PASSKEY_AUTHENTICATION
    } else {
      NextStep.PASSKEY_REGISTRATION
    }

    return LoginResult(pendingToken, nextStep)
  }

  data class LoginResult(val accessToken: String, val nextStep: NextStep)

  enum class NextStep { PASSKEY_REGISTRATION, PASSKEY_AUTHENTICATION }
}
