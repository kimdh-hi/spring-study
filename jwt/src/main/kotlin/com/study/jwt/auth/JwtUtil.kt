package com.study.jwt.auth

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.Ed25519Signer
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
object JwtUtil {

    private val keyPair = OctetKeyPairGenerator(Curve.Ed25519)
        .keyID("jwtSecret")
        .generate()

    fun generateToken(requestVO: JwtRequestVO): String {
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

    fun parseToken() {

    }

    fun verifyExpirationDate() {

    }
}