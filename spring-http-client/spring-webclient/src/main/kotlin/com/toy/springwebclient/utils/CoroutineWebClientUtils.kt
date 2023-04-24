package com.toy.springwebclient.utils

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

@Component
class CoroutineWebClientUtils(
  private val webClient: WebClient
) {

  suspend fun <B : Any, T: Any> post(uri: String, body: B, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): Mono<T> {
    return coroutineScope {
      withContext(this@coroutineScope.coroutineContext) {
        return@withContext webClient.post()
          .uri(uri)
          .headers { header -> headers?.let { header.addAll(it) } }
          .bodyValue(body)
          .retrieve()
          .bodyToMono(returnType.java)
          .doOnError { throw RuntimeException(it.message) }
      }
    }
  }

  suspend fun <T: Any> get(uri: String, params: MultiValueMap<String, String>? = null, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): Mono<T> {
    val uriComponents = UriComponentsBuilder
      .fromUriString(uri)
      .queryParams((params ?: LinkedMultiValueMap()))
      .build()
    return coroutineScope {
      withContext(this@coroutineScope.coroutineContext) {
        return@withContext webClient.get()
          .uri(uriComponents.toUri())
          .headers { header -> headers?.let { header.addAll(it) } }
          .retrieve()
          .bodyToMono(returnType.java)
          .doOnError { throw RuntimeException(it.message) }
      }
    }
  }

  suspend fun <T: Any> getFlux(uri: String, params: MultiValueMap<String, String>? = null, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): Flux<T> {
    val uriComponents = UriComponentsBuilder
      .fromUriString(uri)
      .queryParams((params ?: LinkedMultiValueMap()))
      .build()
    return coroutineScope {
      withContext(this@coroutineScope.coroutineContext) {
        return@withContext webClient.get()
          .uri(uriComponents.toUri())
          .headers { header -> headers?.let { header.addAll(it) } }
          .retrieve()
          .bodyToFlux(returnType.java)
          .doOnError { throw RuntimeException(it.message) }
      }
    }
  }

  suspend fun <B : Any, T: Any> put(uri: String, body: B, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): Mono<T> {
    return coroutineScope {
      withContext(this@coroutineScope.coroutineContext) {
        return@withContext webClient.put()
          .uri(uri)
          .headers { header -> headers?.let { header.addAll(it) } }
          .bodyValue(body)
          .retrieve()
          .bodyToMono(returnType.java)
          .doOnError { throw RuntimeException(it.message) }
      }
    }
  }

  suspend fun <T: Any> delete(uri: String, returnType: KClass<T>, headers: MultiValueMap<String, String>? = null): Mono<T> {
    return coroutineScope {
      withContext(this@coroutineScope.coroutineContext) {
        return@withContext webClient.delete()
          .uri(uri)
          .headers { header -> headers?.let { header.addAll(it) } }
          .retrieve()
          .bodyToMono(returnType.java)
          .doOnError { throw RuntimeException(it.message) }
      }
    }
  }
}