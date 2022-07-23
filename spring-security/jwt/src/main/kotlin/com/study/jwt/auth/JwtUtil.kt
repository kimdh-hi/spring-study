package com.study.jwt.auth

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.Ed25519Signer
import com.nimbusds.jose.crypto.Ed25519Verifier
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator
import com.nimbusds.jose.shaded.json.parser.ParseException
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jwt.util.DateUtils
import com.study.jwt.exception.ErrorCodes
import com.study.jwt.exception.JwtAuthenticationException
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
object JwtUtil {

    private const val SKEW_SECONDS = 3L
    private val keyPair = OctetKeyPairGenerator(Curve.Ed25519)
        .keyID("jwtSecret")
        .generate()

    fun generateToken(requestVO: JwtPrincipal): String {
        val header = JWSHeader.Builder(JWSAlgorithm.EdDSA)
            .type(JOSEObjectType.JWT)
            .build()

        val payload = JWTClaimsSet.Builder()
            .claim("userId", requestVO.userId)
            .claim("username", requestVO.username)
            .claim("authority", requestVO.authority)
            .issueTime(Date())
            .expirationTime(getExpirationTime())
            .build()

        val signedJWT = SignedJWT(header, payload)
        signedJWT.sign(Ed25519Signer(keyPair))

        return signedJWT.serialize()
    }

    private fun getExpirationTime(): Date {
        val expirationTime =  LocalDateTime.now().plusHours(1L).atZone(ZoneId.systemDefault()).toInstant()
        return Date.from(expirationTime)
    }

    fun parseToken(token: String): JwtPrincipal {
        try {
            val signedJWT = SignedJWT.parse(token)
            signedJWT.verify(Ed25519Verifier(keyPair.toPublicJWK()))
            val expirationTime = signedJWT.jwtClaimsSet.expirationTime
            if(!verifyExpirationDate(expirationTime)) throw JwtAuthenticationException("token already expired...", ErrorCodes.INVALID_TOKEN)

            return JwtPrincipal.fromJwtClaims(signedJWT.jwtClaimsSet)
        } catch (e: ParseException) {
            throw JwtAuthenticationException("failed to parsing token...", ErrorCodes.INVALID_TOKEN)
        } catch (e: JOSEException) {
            throw JwtAuthenticationException("failed to parsing token...", ErrorCodes.INVALID_TOKEN)
        }
    }

    fun verifyExpirationDate(date: Date): Boolean {
        return DateUtils.isAfter(date, Date(), SKEW_SECONDS)
    }
}