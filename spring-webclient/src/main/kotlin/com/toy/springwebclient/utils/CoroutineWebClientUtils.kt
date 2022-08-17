//package com.toy.springwebclient.utils
//
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Qualifier
//import org.springframework.stereotype.Component
//import org.springframework.util.MultiValueMap
//import org.springframework.web.reactive.function.client.WebClient
//import org.springframework.web.reactive.function.client.awaitBody
//import kotlin.reflect.KClass
//
//@Component
//@Qualifier("coroutineWebClientUtils")
//class CoroutineWebClientUtils(
//  val webClient: WebClient,
//) {
//
//  private val log = LoggerFactory.getLogger(javaClass)
//
//  suspend fun <B : Any, T: Any> post(uri: String, body: B, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): T {
//    return postInternal(uri, body, returnType, headers)
//  }
//
//  private suspend inline fun <B : Any, reified T: Any> postInternal(uri: String, body: B, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): T {
//    return webClient.post()
//      .uri(uri)
//      .headers { header ->
//        headers?.let { header.addAll(it) }
//      }
//      .bodyValue(body)
//      .retrieve()
//      .awaitBody()
//  }
//}