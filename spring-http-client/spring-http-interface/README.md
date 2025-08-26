## Spring Http Interface

## Custom argument resolver
- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface.custom-resolver
- queryString 으로 객체 요청하는 것 지원 안 됨.
  - OpenFeign 의 경우 @SpringQueryMap 통해 지원
  - feign.QueryMapEncoder 참고
- HttpServiceArgumentResolver 통해 argumentResolver custom 가능

```kotlin
  @Bean
fun myTestApiClient(): MyTestApiClient {
  val restClient = RestClient.builder()
    .baseUrl("http://localhost:8084/test")
    .build()

  val adapter = RestClientAdapter.create(restClient)
  val factory = HttpServiceProxyFactory
    .builderFor(adapter)
    .customArgumentResolver(PingPongQueryArgumentResolver())
    .build()

  return factory.createClient(MyTestApiClient::class.java)
}

class PingPongQueryArgumentResolver : HttpServiceArgumentResolver {
  override fun resolve(
    argument: Any?,
    parameter: MethodParameter,
    requestValues: HttpRequestValues.Builder
  ): Boolean {
    if (parameter.getParameterType() == PingPongDto::class.java) {
      val dto = argument as PingPongDto
      requestValues.addRequestParameter("data", dto.data)
        .addRequestParameter("date", dto.date.toString())
      return true
    }
    return false
  }
}
```
- 매 번 특정 타입에 대한 `HttpServiceArgumentResolver` 작성이 추가되지 않도록 feign 에서 사용하는 방식과 흡사하게 reflection 이용한 방식 적용 필요

### General customArgumentResolver
- list 지원
- 중첩구조 지원x
  - @SpringQueryMap 사용시에도 중첩구조는 지원 안 됨, custom 필요 (https://github.com/spring-cloud/spring-cloud-openfeign/issues/442)
```kotlin
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
```

- openFeign @SpringQueryMap 과 유사
```java
// feign.RequestTemplateFactoryResolver.addQueryMapQueryParameters
private RequestTemplate addQueryMapQueryParameters(
    Map<String, Object> queryMap, RequestTemplate mutable) {
  for (Map.Entry<String, Object> currEntry : queryMap.entrySet()) {
    Collection<String> values = new ArrayList<String>();

    Object currValue = currEntry.getValue();
    if (currValue instanceof Iterable<?>) {
      Iterator<?> iter = ((Iterable<?>) currValue).iterator();
      while (iter.hasNext()) {
        Object nextObject = iter.next();
        values.add(nextObject == null ? null : UriUtils.encode(nextObject.toString()));
      }
    } else if (currValue instanceof Object[]) {
      for (Object value : (Object[]) currValue) {
        values.add(value == null ? null : UriUtils.encode(value.toString()));
      }
    } else {
      if (currValue != null) {
        values.add(UriUtils.encode(currValue.toString()));
      }
    }

    if (values.size() > 0) {
      mutable.query(UriUtils.encode(currEntry.getKey()), values);
    }
  }
  return mutable;
}
```

---

### reference
- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface
