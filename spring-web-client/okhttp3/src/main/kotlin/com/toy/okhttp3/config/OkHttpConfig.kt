package com.toy.okhttp3.config

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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
      .connectTimeout(okHttpProperties.connectTimeoutSecond, TimeUnit.SECONDS)
      .writeTimeout(okHttpProperties.writeAndReadTimeoutSecond, TimeUnit.SECONDS)
      .readTimeout(okHttpProperties.writeAndReadTimeoutSecond, TimeUnit.SECONDS)
      .addInterceptor(DefaultContentTypeInterceptor())
      .build()
  }
}

@ConfigurationProperties(prefix = "okhttp")
@ConstructorBinding
data class OkHttpProperties(
  val connectTimeoutSecond: Long,
  val writeAndReadTimeoutSecond: Long
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