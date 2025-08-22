package com.toy.springhttpinterface.config

import com.toy.springhttpinterface.httpinterface.FakeApiClient
import com.toy.springhttpinterface.httpinterface.MyTestApiClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpRequestValues
import org.springframework.web.service.invoker.HttpServiceArgumentResolver
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import kotlin.reflect.full.memberProperties

@Configuration
class HttpInterfaceConfig {

  @Bean
  fun httpServiceProxyFactory(restClient: RestClient): HttpServiceProxyFactory {
    val adapter = RestClientAdapter.create(restClient)
    return HttpServiceProxyFactory.builderFor(adapter)
      .customArgumentResolver(QueryMapArgumentResolver())
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

class QueryMapArgumentResolver : HttpServiceArgumentResolver {

  override fun resolve(
    argument: Any?,
    parameter: MethodParameter,
    requestValues: HttpRequestValues.Builder
  ): Boolean {
    if (argument == null) return false
    val multiMap = argument.toQueryMap()
    multiMap.forEach { (key, values) ->
      requestValues.addRequestParameter(key, *values.toTypedArray())
    }
    return true
  }
}

fun Any.toQueryMap(): LinkedMultiValueMap<String, String> {
  val map = LinkedMultiValueMap<String, String>()

  this::class.memberProperties.forEach { property ->
    val value = property.getter.call(this) ?: return@forEach
    val key = property.name
    when (value) {
      is Iterable<*> -> value.filterNotNull().forEach { map.add(key, it.toString()) }
      else -> map.add(key, value.toString())
    }
  }

  return map
}
