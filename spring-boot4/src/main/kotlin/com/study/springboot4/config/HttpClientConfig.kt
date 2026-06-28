package com.study.springboot4.config

import com.study.springboot4.context.RequestContextHolder
import com.study.springboot4.httpclients.TestClient
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer
import org.springframework.web.service.registry.HttpServiceGroup
import org.springframework.web.service.registry.ImportHttpServices

@Configuration
//@ImportHttpServices(basePackages = ["com.study.springboot4.httpclients"]) // without group (default group)
@ImportHttpServices(group = "test", types = [TestClient::class])
class HttpClientConfig {

  @Bean
  fun testClientGroupConfigurer(): RestClientHttpServiceGroupConfigurer {
    return RestClientHttpServiceGroupConfigurer { groups ->
      groups.forEachClient { _, builder ->
        builder
          .requestInterceptor(RestClientLoggingInterceptor())
      }

      groups.filterByName("test")
        .forEachClient { _, builder -> builder.baseUrl("https://jsonplaceholder.typicode.com/todos") }
    }
  }
}

private class RestClientLoggingInterceptor : ClientHttpRequestInterceptor {
  private val log = LoggerFactory.getLogger(RestClientLoggingInterceptor::class.java)

  override fun intercept(
    request: HttpRequest,
    body: ByteArray,
    execution: ClientHttpRequestExecution
  ): ClientHttpResponse {
    val requestId = RequestContextHolder.getRequestId()
    val userInfo = RequestContextHolder.getUserInfo()
    val securityUser = SecurityContextHolder.getContext().authentication?.name

    val isPropagated = requestId != null && userInfo != null && securityUser != null
    val threadType = if (Thread.currentThread().isVirtual) "Virtual" else "Platform"

    log.info(
      "[RestClientLoggingInterceptor] Thread={} ({}), RequestId={}, UserInfo={}, SecurityUser={}, " +
      "Method={}, URI={}, Propagated={}",
      Thread.currentThread().threadId(),
      threadType,
      requestId ?: "NULL",
      userInfo ?: "NULL",
      securityUser ?: "NULL",
      request.method,
      request.uri,
      if (isPropagated) "✓" else "✗"
    )

    return execution.execute(request, body)
  }
}
