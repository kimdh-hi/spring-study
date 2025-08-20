package com.study.openfeigncircuitbreaker.feign.fallback

import com.study.openfeigncircuitbreaker.feign.TestClient
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.stereotype.Component

@Component
class TestClientFallback : FallbackFactory<TestClient> {

  override fun create(cause: Throwable): TestClient {
    return object : TestClient {
      override fun test1(status: Int): String = "test1 fallback response"

      override fun test2(status: Int): String = "test2 fallback response"

      override fun withoutFallback(status: Int): String = throw cause
    }
  }
}
