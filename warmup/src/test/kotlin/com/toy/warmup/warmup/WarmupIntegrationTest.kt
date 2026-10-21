package com.toy.warmup.warmup

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WarmupIntegrationTest {

  @Test
  fun `warmup completes against current api spec`() {
  }
}
