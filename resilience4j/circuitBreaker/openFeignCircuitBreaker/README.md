## Resilience4j + OpenFeign

```
If Spring Cloud CircuitBreaker is on the classpath and spring.cloud.openfeign.circuitbreaker.enabled=true, Feign will wrap all methods with a circuit breaker.
```

### dependencies
```
implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
```


### circuitBreaker configs

| name | description                                                         | default      |
|-----------|---------------------------------------------------------------------|--------------|
 | `slidingWindowType`| COUNT_BASED, TIME_BASED                                             | COUNT_BASED  |
| `slidingWindowSize` | 실패율 계산에 사용하는 최근 호출 수 (TIME_BASED 로 설정시 duration 설정)                 | 100          |
| `minimumNumberOfCalls` | 상태 변경을 평가하기 위한 최소 호출 수                                              | 100          |
| `failureRateThreshold` | CLOSE -> OPEN 상태 변경 임계값                                             | 50           |
| `waitDurationInOpenState` | OPEN -> HALF_OPEN 전환되기 전 대기 시간                                      | 60s          |
| `permittedNumberOfCallsInHalfOpenState` | HALF_OPEN 상태에서 허용되는 호출 수 (호출수 충족 전 실패하는 경우 CLOSE 로 상태 변경, 충족시 OPEN) | 10           |


## property base configuration

### application.yml
```yaml
spring:
  cloud:
    openfeign:
      circuitbreaker:
        enabled: true

resilience4j:
  circuitbreaker:
    configs:
      default:
        slidingWindowSize: 5
        minimumNumberOfCalls: 5
        failureRateThreshold: 100
        waitDurationInOpenState: 10s
        permittedNumberOfCallsInHalfOpenState: 10

    instances:
      default:
        baseConfig: default

```

## code base configuration

application.yml
```yaml
spring:
  cloud:
    openfeign:
      circuitbreaker:
        enabled: true
```

```kotlin
@Configuration
class Resilience4jConfig {
  @Bean
  fun defaultCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {
    val customConfig = CircuitBreakerConfig.custom()
      .slidingWindow(5, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
      .failureRateThreshold(100F)
      .waitDurationInOpenState(Duration.ofSeconds(10))
      .permittedNumberOfCallsInHalfOpenState(10)
      .build()

    return Customizer { factory ->
      factory.configureDefault { id ->
        Resilience4JConfigBuilder(id)
          .circuitBreakerConfig(customConfig)
//          .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
          .build()
      }
    }
  }
}
```

## fallback

```kotlin
@FeignClient(name = "testClient", url = "http://localhost:8080/test", fallbackFactory = TestClientFallback::class)
interface TestClient {
  @GetMapping("/hello")
  fun hello(): String

  @GetMapping("/hello2")
  fun hello2(): String
}

@Component
class TestClientFallback : FallbackFactory<TestClient> {

  override fun create(cause: Throwable): TestClient {
    return object : TestClient {
      override fun hello(ex: Boolean): String = "hello fallback response"

      override fun hello2(ex: Boolean): String = throw cause // ignore
    }
  }
}

```


## CircuitBreakerNameResolver
- https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#spring-cloud-feign-circuitbreaker
- default: DefaultCircuitBreakerNameResolver
  - `TestClient#hello(boolean)`
  `<feignClientClassName>#<calledMethod>(<parameterTypes>)`
- CircuitBreakerNameResolver 결과 string 마다 circuit 이 할당. (없는 경우 생성)
  - 즉, default 는 각 feignClient 의 메서드마다 circuit 이 할당.
- target url 의 host 마다 circuit 생성 가능 
```kotlin
// CircuitBreakerNameResolver custom
@Bean
fun circuitBreakerNameResolver(): CircuitBreakerNameResolver {
  return CircuitBreakerNameResolver { _, target, method ->
    val url = target.url()
    runCatching {
      URL(url).host
    }.getOrElse {
      log.warn("resolve circuitBreakerName failed. malformed url. url={}", url)
      Feign.configKey(target.type(), method)
    }
  }
}
```
- 호출 대상 서버가 kubernetes 환경에서 내부통신을 위해 service 를 통하는 경우 혹은 lb 를 통하여 실제 호출 대상 host 등을 식별할 수 없는 경우 대상 마다 feignClient 를 분리하고 feignClientName 마다 circuit 을 생성 가능
```kotlin
@Bean
fun circuitBreakerNameResolver(): CircuitBreakerNameResolver {
  return CircuitBreakerNameResolver { feignClientName, _, _ -> feignClientName }
}
```


---

Reference
- https://docs.spring.io/spring-cloud-circuitbreaker/reference/spring-cloud-circuitbreaker-resilience4j/starters.html
- https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#spring-cloud-feign-circuitbreaker
