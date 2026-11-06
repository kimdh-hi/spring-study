package com.toy.dpopproofjwt.infra.security

import com.nimbusds.jose.JWSObject
import com.toy.dpopproofjwt.application.auth.IssuedToken
import com.toy.dpopproofjwt.application.auth.TokenIssuer
import com.toy.dpopproofjwt.application.auth.TokenType
import com.toy.dpopproofjwt.common.exception.InvalidDPoPProofException
import com.toy.dpopproofjwt.common.exception.UseDPoPNonceException
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.DPoPProofContext
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoderFactory
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class DPoPJwtTokenIssuer(
  private val jwtEncoder: JwtEncoder,
  private val dPoPProofDecoderFactory: JwtDecoderFactory<DPoPProofContext>,
) : TokenIssuer {

  private val ttl: Duration = Duration.ofMinutes(5)

  override fun issue(subject: String, dPoPProof: String, method: String, targetUri: String): IssuedToken {
    val jkt = verifyAndThumbprint(dPoPProof, method, targetUri)
    val now = Instant.now()
    val claims = JwtClaimsSet.builder()
      .subject(subject)
      .issuedAt(now)
      .expiresAt(now.plus(ttl))
      .claim("cnf", mapOf("jkt" to jkt))
      .build()

    val jwt = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.ES256).build(), claims))

    return IssuedToken(jwt.tokenValue, TokenType.DPOP, ttl.seconds)
  }

  private fun verifyAndThumbprint(dPoPProof: String, method: String, targetUri: String): String {
    try {
      val context = DPoPProofContext.withDPoPProof(dPoPProof)
        .method(method)
        .targetUri(targetUri)
        .build()

      dPoPProofDecoderFactory.createDecoder(context).decode(dPoPProof)
    } catch (ex: Exception) {
      if (ex.isUseDPoPNonce()) throw UseDPoPNonceException(ex)
      throw InvalidDPoPProofException(ex.message, ex)
    }

    return JWSObject.parse(dPoPProof).header.jwk.computeThumbprint().toString()
  }
}
