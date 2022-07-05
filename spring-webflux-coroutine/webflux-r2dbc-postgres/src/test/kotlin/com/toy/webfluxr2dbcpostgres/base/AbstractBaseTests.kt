package com.toy.webfluxr2dbcpostgres.base

import com.google.auto.service.AutoService
import com.toy.webfluxr2dbcpostgres.auth.JwtUtil
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration
import java.io.FilterInputStream

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractBaseTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(CustomBlockHoundIntegration::class)
abstract class AbstractIntegrationTest: AbstractBaseTest() {

  @Autowired lateinit var webTestClient: WebTestClient
  @Autowired lateinit var jwtUtil: JwtUtil

}

@AutoService(BlockHoundIntegration::class)
class CustomBlockHoundIntegration: BlockHoundIntegration {

  override fun applyTo(builder: BlockHound.Builder) {
    builder.allowBlockingCallsInside(FilterInputStream::class.java.name, "read")
  }
}