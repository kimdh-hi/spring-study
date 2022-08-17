package com.toy.springwebclient.utils

import org.slf4j.LoggerFactory
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

class WebClientUtils(
  private val webClient: WebClient
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun <B : Any, T: Any> post(uri: String, body: B, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): Mono<T> {
    return webClient.post()
      .uri(uri)
      .headers { header ->
        headers?.let { header.addAll(it) }
      }
      .bodyValue(body)
      .retrieve()
      .bodyToMono(returnType.java)
      .onErrorResume { Mono.error { RuntimeException("webclient error...") } }
  }

  fun <T: Any> get(uri: String, returnType: KClass<T>, params: MultiValueMap<String, String>? = null, headers: MultiValueMap<String, String>? = null): Mono<T> {
    return webClient.get()
      .uri { it
        .queryParams(params ?: LinkedMultiValueMap())
        .path(uri)
        .build()
      }
      .headers { header ->
        headers?.let { header.addAll(it) }
      }
      .retrieve()
      .bodyToMono(returnType.java)
      .onErrorResume { Mono.error { RuntimeException("webclient error...") } }
  }

  fun <B : Any, T: Any> put(uri: String, body: B, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): Mono<T> {
    return webClient.put()
      .uri(uri)
      .headers { header ->
        headers?.let { header.addAll(it) }
      }
      .bodyValue(body)
      .retrieve()
      .bodyToMono(returnType.java)
      .onErrorResume { Mono.error { RuntimeException("webclient error...") } }
  }

  fun delete(uri: String, headers: MultiValueMap<String, String>? = null) {
    webClient.delete()
      .uri(uri)
      .headers { header ->
        headers?.let { header.addAll(it) }
      }
      .retrieve()
      .bodyToMono(Unit::class.java)
      .onErrorResume { Mono.error { RuntimeException("webclient error...") } }
      .doAfterTerminate { log.debug("[AdvancedWebClientUtils] DELETE COMPLETED") }
      .subscribe()
  }
}