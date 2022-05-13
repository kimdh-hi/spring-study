package com.study.jwt

import org.springframework.security.crypto.factory.PasswordEncoderFactories

object PasswordUtil {

    private val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    fun encode(plainPassword: String) = encoder.encode(plainPassword)

    fun matches(plainPassword: String, encPassword: String) = encoder.matches(plainPassword, encPassword)
}