package com.toy.okhttp3.utils

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class OkHttpUtils(
  private val client: OkHttpClient
) {

  fun <T : Any> post(url: String, headers: Map<String, String>? = null, returnType: KClass<T>, body: Any ): T {
    val jsonBody = toJson(body)
    val request = Request.Builder()
      .url(url)
      .headers(createHeader(headers))
      .post(jsonBody.toRequestBody())
      .build()

    val responseBody = execute(request)
    val responseBodyString = responseBody.string()

    return fromJsonToObj(responseBodyString, returnType)
  }

  fun <T: Any> get(
    url: String, headers: Map<String, String>? = null, returnType: KClass<T>, params: Map<String, String>? = null
  ): T {
    val request = Request.Builder()
      .url(url)
      .headers(createHeader(headers))
      .get()
      .build()

    val responseBody = execute(request)
    val responseBodyString = responseBody.string()

    return fromJsonToObj(responseBodyString, returnType)
  }

  private fun createHeader(headers: Map<String, String>?): Headers {
    val headerBuilder = Headers.Builder()
    headers?.let { header ->
      header.forEach { (t, u) -> headerBuilder.add(t, u) }
    }
    return headerBuilder.build()
  }

  private fun execute(request: Request): ResponseBody {
    return client.newCall(request)
      .execute()
      .body ?: throw RuntimeException("okHttp error ...")
  }
}