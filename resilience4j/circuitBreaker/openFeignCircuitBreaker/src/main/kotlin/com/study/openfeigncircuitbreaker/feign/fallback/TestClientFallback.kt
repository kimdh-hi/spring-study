package com.study.openfeigncircuitbreaker.feign.fallback

import com.study.openfeigncircuitbreaker.feign.TestClient
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.stereotype.Component

@Component
class TestClientFallback : FallbackFactory<TestClient> {

  override fun create(cause: Throwable): TestClient {
    return object : TestClient {
      override fun hello(ex: Boolean): String = "hello fallback response"

      override fun hello2(ex: Boolean): String = throw cause
    }
  }
}
