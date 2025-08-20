## Resilience4j + OpenFeign

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


## property base setting

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

## code bas setting

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

---

Reference
- https://docs.spring.io/spring-cloud-circuitbreaker/reference/spring-cloud-circuitbreaker-resilience4j/starters.html
- https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#spring-cloud-feign-circuitbreaker
