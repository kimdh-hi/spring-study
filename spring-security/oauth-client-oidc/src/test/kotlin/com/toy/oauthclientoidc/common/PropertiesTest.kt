package com.toy.oauthclientoidc.common

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class PropertiesTest(
  val oauth2Properties: Oauth2Properties
) {

  @Test
  fun oauth2Properties() {
    val registration = oauth2Properties.registration
    println(registration)
  }
}