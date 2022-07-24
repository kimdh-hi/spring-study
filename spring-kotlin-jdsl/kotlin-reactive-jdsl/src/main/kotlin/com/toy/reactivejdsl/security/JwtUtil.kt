package com.toy.reactivejdsl.security

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.Ed25519Signer
import com.nimbusds.jose.crypto.Ed25519Verifier
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jwt.util.DateUtils
import com.toy.reactivejdsl.domain.User
import com.toy.webfluxr2dbcpostgres.auth.JwtPrincipal
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class JwtUtil {
  private val SKEW_SECONDS = 3L
  private val keyPair = OctetKeyPairGenerator(Curve.Ed25519)
    .keyID("secret")
    .generate()

  fun createToken(user: User): String {
    val header = JWSHeader.Builder(JWSAlgorithm.EdDSA)
      .type(JOSEObjectType.JWT)
      .build()

    val payload = JWTClaimsSet.Builder()
      .issueTime(Date())
      .claim(JwtPrincipal.CLAIM_USER_ID, user.id.toString())
      .claim(JwtPrincipal.CLAIM_USERNAME, user.username)
      .expirationTime(getExpirationTime())
      .build()

    val signedJWT = SignedJWT(header, payload)
    signedJWT.sign(Ed25519Signer(keyPair))

    return signedJWT.serialize()
  }

  fun parse(token: String): JwtPrincipal {
    try {
      val signedJWT = SignedJWT.parse(token)
      signedJWT.verify(Ed25519Verifier(keyPair.toPublicJWK()))
      if (!checkTokenExpirationTime(signedJWT.jwtClaimsSet.expirationTime)) throw RuntimeException("token error")

      return JwtPrincipal.newPrincipal(signedJWT.jwtClaimsSet)
    } catch (e: com.nimbusds.jose.shaded.json.parser.ParseException) {
      throw throw RuntimeException("token error")
    } catch (e: JOSEException) {
      throw throw RuntimeException("token error")
    }

  }

  private fun checkTokenExpirationTime(date: Date): Boolean {
    return DateUtils.isAfter(date, Date(), SKEW_SECONDS)
  }

  private fun getExpirationTime(): Date {
    val expirationTime =  LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(expirationTime)
  }

}
