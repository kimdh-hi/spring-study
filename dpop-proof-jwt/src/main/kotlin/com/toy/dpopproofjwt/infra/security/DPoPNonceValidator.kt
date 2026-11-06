package com.toy.dpopproofjwt.infra.security

import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtValidationException

const val DPOP_NONCE_HEADER = "DPoP-Nonce"
const val USE_DPOP_NONCE = "use_dpop_nonce"

class DPoPNonceValidator(
  private val nonceProvider: DPoPNonceProvider,
) : OAuth2TokenValidator<Jwt> {

  override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
    val nonce = jwt.getClaimAsString("nonce")
    if (nonce != null && nonceProvider.isValid(nonce)) return OAuth2TokenValidatorResult.success()

    return OAuth2TokenValidatorResult.failure(
      OAuth2Error(USE_DPOP_NONCE, "DPoP proof requires a valid server-provided nonce.", null),
    )
  }
}

fun Throwable?.isUseDPoPNonce(): Boolean =
  this is JwtValidationException && errors.isNotEmpty() && errors.all { it.errorCode == USE_DPOP_NONCE }
