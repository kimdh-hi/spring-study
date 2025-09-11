## Resilience4J

### CircuitBreaker

#### circuitBreaker configs

| name                                                  | description                                                         | default     |
|-------------------------------------------------------|---------------------------------------------------------------------|-------------|
| `slidingWindowType`                                   | COUNT_BASED, TIME_BASED                                             | COUNT_BASED |
| `slidingWindowSize`                                   | 실패율 계산에 사용하는 최근 호출 수 (TIME_BASED 로 설정시 duration 설정)                 | 100         |
| `minimumNumberOfCalls`                                | 상태 변경을 평가하기 위한 최소 호출 수                                              | 100         |
| `failureRateThreshold`                                | CLOSE -> OPEN 상태 변경 임계값                                             | 50          |
| `waitDurationInOpenState`                             | OPEN -> HALF_OPEN 전환되기 전 최소 대기 시간                                   | 60s         |
 | `automatic-transition-from-open-to-half-open-enabled` | waitDurationInOpenState 경과 후 OPEN -> HALF_OPEN 여부                   | false       |
| `permittedNumberOfCallsInHalfOpenState`               | HALF_OPEN 상태에서 허용되는 호출 수 (호출수 충족 전 실패하는 경우 CLOSE 로 상태 변경, 충족시 OPEN) | 10          |


#### exception record condition
- http client 통해 호출한 대상 서버에서 비즈니스 로직에 따라 잘못된 요청인 경우 예외 응답 가능
  - 위 예외를 circuit의 실패율에 포함시킬지?
  - 예를 들어, 권한이 없는 상태로 연속해서 api 호출하는 경우 호출 대상 서버에는 문제가 없지만 circuit 이 열릴 수 있음. 이게 맞는가 고민 필요.
- business exception 은 http status code 400, 그 외 서버 내부 에러인 경우 http status code 500 으로 구분되어 있는 경우 아래처럼 실패수 기록되도록 설정 가능

```kotlin
class RestClientCircuitRecordFailurePredicate : Predicate<Throwable> {
  override fun test(t: Throwable): Boolean {
    return when (t) {
      is HttpServerErrorException -> t.statusCode.value() >= 500
      else -> false
    }
  }
}
```

```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        record-failure-predicate: com.study.resilience4j.config.RestClientCircuitRecordFailurePredicate
```

##### CallNotPermittedException
- circuit OPEN or HALF_OPEN 된 경우 요청시 fallback 지정되지 않은 경우 CallNotPermittedException 응답
- exception handler 추가

```kotlin
@ExceptionHandler
fun handle(ex: CallNotPermittedException): ResponseEntity<ErrorResponse> {
  log.warn("CallNotPermittedException ex={}", ex.message) // CallNotPermittedException ex=CircuitBreaker 'test1' is OPEN and does not permit further calls
  return ResponseEntity(ErrorResponse(1234, "CallNotPermittedException"), HttpStatus.INTERNAL_SERVER_ERROR)
}
```

#### register-health-indicator
- `/actuator/circuitbreakers`

```yaml
management:
  endpoints:
    web:
      exposure:
        include: circuitbreakers
```

```
{
  "circuitBreakers": {
    "default": {
      "failureRate": "-1.0%",
      "slowCallRate": "-1.0%",
      "failureRateThreshold": "100.0%",
      "slowCallRateThreshold": "100.0%",
      "bufferedCalls": 0,
      "failedCalls": 0,
      "slowCalls": 0,
      "slowFailedCalls": 0,
      "notPermittedCalls": 0,
      "state": "CLOSED"
    },
    "test1": {
      "failureRate": "100.0%",
      "slowCallRate": "0.0%",
      "failureRateThreshold": "100.0%",
      "slowCallRateThreshold": "100.0%",
      "bufferedCalls": 5,
      "failedCalls": 5,
      "slowCalls": 0,
      "slowFailedCalls": 0,
      "notPermittedCalls": 6,
      "state": "OPEN"
    }
  }
}
```


#### CircuitBreaker Event
- https://resilience4j.readme.io/docs/circuitbreaker

- document example
```java
Map <String, String> circuitBreakerTags = Map.of("key1", "value1", "key2", "value2");

CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.custom()
    .withCircuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
    .addRegistryEventConsumer(new RegistryEventConsumer() {
        @Override
        public void onEntryAddedEvent(EntryAddedEvent entryAddedEvent) {
            // implementation
        }
        @Override
        public void onEntryRemovedEvent(EntryRemovedEvent entryRemoveEvent) {
            // implementation
        }
        @Override
        public void onEntryReplacedEvent(EntryReplacedEvent entryReplacedEvent) {
            // implementation
        }
    })
    .withTags(circuitBreakerTags)
    .build();

CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("testName");
```

- fallback 설정된 경우 open 대상으로 요청시에도 CallNotPermittedException 로 핸들링 불가.
- fallback 이 설정된 경우에도 open 된 경우 인지 가능하도록 로그 및 후처리 가능해야 함

```kotlin
@Bean
fun customRegistryEventConsumer(): RegistryEventConsumer<CircuitBreaker> {
  return object : RegistryEventConsumer<CircuitBreaker> {
    override fun onEntryAddedEvent(entryAddedEvent: EntryAddedEvent<CircuitBreaker>) {
      val eventPublisher = entryAddedEvent.addedEntry.eventPublisher

      eventPublisher.onCallNotPermitted {
        log.error("[CircuitBreakerEvent.callNotPermitted] circuitName={}", it.circuitBreakerName)
      }

      eventPublisher.onStateTransition {
        val stateTransition = it.stateTransition
        log.info(
          "[CircuitBreakerEvent.stateTransition] {} state {} -> {}",
          it.circuitBreakerName,
          stateTransition.fromState,
          stateTransition.toState
        )
      }
    }

    override fun onEntryRemovedEvent(entryRemoveEvent: EntryRemovedEvent<CircuitBreaker>) {}

    override fun onEntryReplacedEvent(entryReplacedEvent: EntryReplacedEvent<CircuitBreaker>) {}
  }
}
```

#### interface 내 fallback 함수 저의 이슈 (default implementation in interface issue)

```kotlin
interface Test2Client {

  @GetExchange("/test/test1")
  @CircuitBreaker(name = "test2", fallbackMethod = "fallback")
  fun test1(@RequestParam status: Int): String

  fun fallback(status: Int, ex: Throwable): String {
    return "/test/test1 fallback"
  }
```

java 코드 변환 결과

```java
public interface Test2Client {
   @GetExchange("/test/test1")
   @CircuitBreaker(
      name = "test2",
      fallbackMethod = "fallback"
   )
   @NotNull
   String test1(@RequestParam int var1);

   @NotNull
   String fallback(int var1, @NotNull Throwable var2);

   @GetExchange("/test/test2")
   @CircuitBreaker(
      name = "test2"
   )
   @NotNull
   String test2(@RequestParam int var1);

   @Metadata(
      mv = {1, 9, 0},
      k = 3,
      xi = 48
   )
   public static final class DefaultImpls {
      @NotNull
      public static String fallback(@NotNull Test2Client $this, int status, @NotNull Throwable ex) {
         Intrinsics.checkNotNullParameter(ex, "ex");
         return "/test/test1 fallback";
      }
   }
}
```

fallbackMethod 생성시 이슈
- @CircuitBreaker 기준 동일 클래스 내 fallback 메서드 이름, 인자, 반환타입 등으로 fallbackMethod 생성
- kotlin 에서 구현을 갖는 함수를 interface 에 정의시 위 처럼 별도 class 가 생기고 내부에 선언되므로 위 fallbackMethod 로 찾아질 수 없음.
- java 의 interface default 와 동일하게 동작 가능하도록 compile option 추가 필요

```kotlin
// kotlin 1.x
kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjvm-default=all")
  }
}

//kotlin 2.x
//default: JvmDefaultMode.ENABLE
kotlin {
  compilerOptions {
    jvmDefault = JvmDefaultMode.NO_COMPATIBILITY
  }
}

//JvmDefaultMode.ENABLE
// 하위호환 위한 DefaultImpls 와 default 메서드 모두 생성
// JvmDefaultMode.NO_COMPATIBILITY 는 default 메서드 만 생성 (DefaultImpls x)
public interface AAA {
   void aaa();

   @NotNull
   default String bbb() {
      System.out.println("bbb");
      return "bbb";
   }

   public static final class DefaultImpls {
      /** @deprecated */
      @Deprecated
      @NotNull
      public static String bbb(@NotNull AAA $this) {
         return $this.bbb();
      }
   }
}

```



---

## reference
- https://docs.spring.io/spring-cloud-circuitbreaker/reference/spring-cloud-circuitbreaker-resilience4j.html
- https://resilience4j.readme.io/docs/circuitbreaker
