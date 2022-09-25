package com.lecture.userservice.utils

import com.lecture.userservice.config.JwtProperties
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class JwtUtilsTest {
  private val log = KotlinLogging.logger {  }

  @Test
  fun createToken() {
    // given
    val jwtClaim = JwtClaim(
      userId = 999,
      username = "test-username",
      name = "test-name",
      profileUrl = "test.jpg"
    )

    val jwtProperties = JwtProperties(
      issuer = "test-issuer",
      subject = "test-subject",
      expiresHours = 1,
      secret = "test-secret"
    )

    //when
    val token = JwtUtils.createToken(jwtClaim, jwtProperties)

    //then
    assertAll({
      assertNotNull(token)
      assertEquals(3, token.split(".").size)
    })
    log.info { token }
  }

  @Test
  fun decode() {
    // given
    val jwtClaim = JwtClaim(
      userId = 999,
      username = "test-username",
      name = "test-name",
      profileUrl = "test.jpg"
    )

    val jwtProperties = JwtProperties(
      issuer = "test-issuer",
      subject = "test-subject",
      expiresHours = 1,
      secret = "test-secret"
    )
    val token = JwtUtils.createToken(jwtClaim, jwtProperties)

    //when
    val decodedToken = JwtUtils.decode(token, "test-secret", issuer = "test-issuer")

    //then
    val claims = decodedToken.claims
    assertAll({
      assertEquals(999, claims["userId"]!!.asLong())
      assertEquals("test-username", claims["username"]!!.asString())
    })
  }
}