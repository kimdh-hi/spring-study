package com.study.springboot4.config

import com.study.springboot4.httpclients.TestClient
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer
import org.springframework.web.service.registry.HttpServiceGroup
import org.springframework.web.service.registry.ImportHttpServices

@Configuration(proxyBeanMethods = false)
//@ImportHttpServices(basePackages = ["com.study.springboot4.httpclients"]) // without group (default group)
@ImportHttpServices(group = "test", types = [TestClient::class])
class HttpClientConfig {

  @Bean
  fun testClientGroupConfigurer() = RestClientHttpServiceGroupConfigurer {
    it.forEachClient { group, clientBuilder ->
      clientBuilder.defaultHeader("group-name", group.name())
        .requestInterceptor(RestClientLoggingInterceptor())
    }
  }

  private class RestClientLoggingInterceptor() : ClientHttpRequestInterceptor {
    private val log = LoggerFactory.getLogger(RestClientLoggingInterceptor::class.java)

    override fun intercept(
      request: HttpRequest,
      body: ByteArray,
      execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
      log.info(
        "httpClient request: [{}] {} groupHeader={} version={}",
        request.method,
        request.uri,
        request.headers["group-name"],
        request.headers["X-Version"]
      )
      return execution.execute(request, body)
    }
  }
}
