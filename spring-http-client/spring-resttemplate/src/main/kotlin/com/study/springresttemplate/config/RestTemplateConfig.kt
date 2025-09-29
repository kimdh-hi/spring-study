package com.study.springresttemplate.config

import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.util.TimeValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.time.Duration


@Configuration
class RestTemplateConfig {

  @Bean
  fun restTemplate(): RestTemplate {
    val factory = HttpComponentsClientHttpRequestFactory().apply {
      httpClient = createHttpClient()
      setConnectTimeout(Duration.ofMinutes(2))
      setReadTimeout(Duration.ofMinutes(2))
    }
    return RestTemplate(factory)
  }

  private fun createHttpClient(): HttpClient {
    val connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
      .setMaxConnTotal(100)
      .setMaxConnPerRoute(100)
      .setDefaultConnectionConfig(
        ConnectionConfig.custom()
          .setTimeToLive(TimeValue.ofMinutes(10))
          .build()
      )
      .build()

    return HttpClientBuilder.create()
      .setConnectionManager(connectionManager)
      .build()
  }
}
