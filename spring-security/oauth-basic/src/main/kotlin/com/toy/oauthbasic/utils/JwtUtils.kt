package com.toy.oauthbasic.utils

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.Ed25519Signer
import com.nimbusds.jose.crypto.Ed25519Verifier
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jose.jwk.OctetKeyPair
import com.nimbusds.jose.produce.JWSSignerFactory
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jwt.util.DateUtils
import com.toy.oauthbasic.config.JwtProperties
import com.toy.oauthbasic.domain.User
import com.toy.oauthbasic.oauth2.JwtPrincipal
import com.toy.oauthbasic.oauth2.JwtPrincipal.Companion.CLAIM_USERNAME
import com.toy.oauthbasic.oauth2.JwtPrincipal.Companion.CLAIM_USER_ID
import org.springframework.stereotype.Component
import java.text.ParseException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class JwtUtils(
  private val jwtProperties: JwtProperties
) {

//  private val octetKeyPair = OctetKeyPair.parse(jwtProperties.jwtKey)
//  private val publicKey = octetKeyPair.toPublicJWK()

  val signer = MACSigner(jwtProperties.jwtKey)

  fun generateToken(user: User): String {
    return generateToken(JwtPrincipal.of(user))
  }

  fun generateToken(jwtPrincipal: JwtPrincipal) : String {
    val header = JWSHeader.Builder(JWSAlgorithm.HS256)
      .type(JOSEObjectType.JWT)
      .build()

    val payload = JWTClaimsSet.Builder()
      .claim(CLAIM_USER_ID, jwtPrincipal.userId)
      .claim(CLAIM_USERNAME, jwtPrincipal.username)
      .issueTime(Date())
      .expirationTime(expirationTime())
      .build()

    val signedJWT = SignedJWT(header, payload)
    signedJWT.sign(signer)

    return signedJWT.serialize()
  }

  private fun expirationTime() : Date {
    val jwtTokenExpireDateTime = LocalDateTime.now().plusHours(jwtProperties.jwtAuthTokenExpiryHours)
    return Date.from(jwtTokenExpireDateTime.atZone(ZoneId.systemDefault()).toInstant())
  }

  fun parseToken(token: String): JwtPrincipal {
    verify(token)
    val signedJWT = SignedJWT.parse(token)
    val jwtClaimsSet = signedJWT.jwtClaimsSet
    return JwtPrincipal.newPrincipal(jwtClaimsSet)
  }

  fun verify(token: String) {
    try {
      val signedJWT = SignedJWT.parse(token)

      if (!signedJWT.verify(MACVerifier(jwtProperties.jwtKey)))
        throw RuntimeException("token invalid..")

      val jwtClaimsSet = signedJWT.jwtClaimsSet
      if (!verifyExpirationDateClaim(jwtClaimsSet.expirationTime))
        throw RuntimeException("token expired..")
    } catch (e: ParseException) {
      throw RuntimeException("token invalid..", e)
    } catch (e: JOSEException) {
      throw RuntimeException("token invalid..", e)
    }
  }

  private fun verifyExpirationDateClaim(expirationTime: Date) = DateUtils.isAfter(expirationTime, Date(), SKEW_SECONDS)

  companion object {
    const val SKEW_SECONDS = 30L
  }

}