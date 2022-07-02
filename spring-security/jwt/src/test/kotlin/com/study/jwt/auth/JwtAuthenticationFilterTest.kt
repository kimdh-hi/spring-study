package com.study.jwt.auth

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class JwtAuthenticationFilterTest {

    @Test
    fun bearerPrefixSubstringTest() {
        //given
        val prefix = "bearer "
        val token = "token"
        val bearerToken = "${prefix}${token}"

        //expect
        assertEquals(token, bearerToken.substring(prefix.length))
    }
}