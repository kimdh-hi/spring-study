package com.toy.oauthclientoidc.auth

import com.nimbusds.jwt.SignedJWT
import com.toy.oauthclientoidc.common.Idp
import org.apache.commons.lang3.StringUtils
import java.text.ParseException
import kotlin.jvm.Throws


class GoogleOidcVerifier {

  fun isGoogleToken(idToken: String): Boolean {
    return try {
      val signedJwt = parse(idToken)
      StringUtils.equalsIgnoreCase(Idp.GOOGLE.name, signedJwt.jwtClaimsSet.issuer)
    } catch (e: ParseException) {
      false
    }
  }

  fun getJwtPrincipal(idToken: String) {
    val signedJWT = try {
      parse(idToken)
    } catch (e: ParseException) {
      throw RuntimeException("token parsing error...")
    }

    val claims = signedJWT.jwtClaimsSet.claims
  }

  @Throws(ParseException::class)
  private fun parse(idToken: String): SignedJWT = SignedJWT.parse(idToken)
}