package com.toy.okhttp3.utils

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException
import kotlin.reflect.KClass

@Component
class OkHttpUtils(
  private val client: OkHttpClient
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun <T : Any> post(url: String, headers: Map<String, String>? = null, returnType: KClass<T>, body: Any): T {
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

  fun <T: Any> postAsyc(url: String, headers: Map<String, String>? = null, returnType: KClass<T>, body: Any) {
    val jsonBody = toJson(body)
    val request = Request.Builder()
      .url(url)
      .headers(createHeader(headers))
      .post(jsonBody.toRequestBody())
      .build()

    client.newCall(request).enqueue(object: Callback {
      override fun onFailure(call: Call, e: IOException) {
        log.error("okHttp error...", e)
        throw RuntimeException("okHttp error...")
      }

      override fun onResponse(call: Call, response: Response) {
        response.use {
          if (it.body == null)
            throw RuntimeException("okHttp error...")
          log.info("body.string {}", it.body!!.string())
        }
      }
    })
  }

  fun <T: Any> get(
    url: String, headers: Map<String, String>? = null, returnType: KClass<T>, params: Map<String, String>? = null
  ): T {
    val httpUrl = url.toHttpUrl().newBuilder()
      .let {
        params?.let { param ->
          params.keys.forEach { key ->
            it.addQueryParameter(key, param[key])
          }
        }
        it
      }.build()

    val request = Request.Builder()
      .url(httpUrl)
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