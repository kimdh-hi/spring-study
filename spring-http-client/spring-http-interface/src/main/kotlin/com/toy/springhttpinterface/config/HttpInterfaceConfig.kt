package com.toy.springhttpinterface.config

import com.toy.springhttpinterface.httpinterface.FakeApiClient
import com.toy.springhttpinterface.httpinterface.MyTestApiClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpRequestValues
import org.springframework.web.service.invoker.HttpServiceArgumentResolver
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Configuration
class HttpInterfaceConfig {

  @Bean
  fun httpServiceProxyFactory(restClient: RestClient): HttpServiceProxyFactory {
    val adapter = RestClientAdapter.create(restClient)
    return HttpServiceProxyFactory.builderFor(adapter)
      .customArgumentResolver(QueryMapHttpServiceArgumentResolver())
      .build()
  }

  @Bean
  fun fakeApiCaller(factory: HttpServiceProxyFactory): FakeApiClient {
    return factory.createClient(FakeApiClient::class.java)
  }

  @Bean
  fun myTestApiClient(factory: HttpServiceProxyFactory): MyTestApiClient {
    return factory.createClient(MyTestApiClient::class.java)
  }
}

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueryMap

private class QueryMapHttpServiceArgumentResolver : HttpServiceArgumentResolver {
  override fun resolve(argument: Any?, parameter: MethodParameter, requestValues: HttpRequestValues.Builder): Boolean {
    if (!parameter.hasParameterAnnotation(QueryMap::class.java)) return false
    requireNotNull(argument) { "argument cannot be null" }

    argument.toMap().forEach { (key, values) ->
      values.forEach { value -> requestValues.addRequestParameter(key, value) }
    }

    return true
  }

  private fun Any.toMap(): Map<String, List<String?>> {
    return this::class.memberProperties.associate { prop ->
      prop.name to prop.toValues(this)
    }
  }

  private fun KProperty1<out Any, *>.toValues(prop: Any): List<String?> {
    val value = getter.call(prop) ?: return emptyList()
    return when (value) {
      is Iterable<*> -> value.map { it?.toString() }
      is Array<*> -> value.map { it?.toString() }
      else -> listOf(value.toString())
    }
  }
}
