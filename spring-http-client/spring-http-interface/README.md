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

---

### reference
- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface
