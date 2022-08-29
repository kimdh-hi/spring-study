package com.toy.okhttp3.config

import okhttp3.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.util.concurrent.TimeUnit

@Configuration
class OkHttpConfig(
  private val okHttpProperties: OkHttpProperties
) {

  @Bean
  fun okHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .connectTimeout(okHttpProperties.connectTimeoutSeconds, TimeUnit.SECONDS)
      .writeTimeout(okHttpProperties.writeAndReadTimeoutSeconds, TimeUnit.SECONDS)
      .readTimeout(okHttpProperties.writeAndReadTimeoutSeconds, TimeUnit.SECONDS)
      .connectionPool(connectionPool())
      .retryOnConnectionFailure(false)
      .addInterceptor(DefaultContentTypeInterceptor())
      .build()
  }

  fun connectionPool(): ConnectionPool {
    return ConnectionPool(
      maxIdleConnections = okHttpProperties.maxIdleConnection,
      keepAliveDuration = okHttpProperties.keepAliveDurationMinutes,
      timeUnit = TimeUnit.MINUTES
    )
  }
}

@ConfigurationProperties(prefix = "okhttp")
@ConstructorBinding
data class OkHttpProperties(
  val connectTimeoutSeconds: Long,
  val writeAndReadTimeoutSeconds: Long,
  val maxIdleConnection: Int,
  val keepAliveDurationMinutes: Long
)

class DefaultContentTypeInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest: Request = chain.request()
    val requestWithUserAgent: Request = originalRequest
      .newBuilder()
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build()
    return chain.proceed(requestWithUserAgent)
  }
}