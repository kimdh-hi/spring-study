## Resilience4J

### CircuitBreaker

#### circuitBreaker configs

| name | description                                                         | default      |
|-----------|---------------------------------------------------------------------|--------------|
| `slidingWindowType`| COUNT_BASED, TIME_BASED                                             | COUNT_BASED  |
| `slidingWindowSize` | 실패율 계산에 사용하는 최근 호출 수 (TIME_BASED 로 설정시 duration 설정)                 | 100          |
| `minimumNumberOfCalls` | 상태 변경을 평가하기 위한 최소 호출 수                                              | 100          |
| `failureRateThreshold` | CLOSE -> OPEN 상태 변경 임계값                                             | 50           |
| `waitDurationInOpenState` | OPEN -> HALF_OPEN 전환되기 전 대기 시간                                      | 60s          |
| `permittedNumberOfCallsInHalfOpenState` | HALF_OPEN 상태에서 허용되는 호출 수 (호출수 충족 전 실패하는 경우 CLOSE 로 상태 변경, 충족시 OPEN) | 10           |


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
- circuit OPEN 된 후 요청시 fallback 지정되지 않은 경우 CallNotPermittedException 응답
- exception handler 추가

```kotlin
@ExceptionHandler
fun handle(ex: CallNotPermittedException): ResponseEntity<ErrorResponse> {
  log.warn("CallNotPermittedException ex={}", ex.message) // CallNotPermittedException ex=CircuitBreaker 'test1' is OPEN and does not permit further calls
  return ResponseEntity(ErrorResponse(1234, "CallNotPermittedException"), HttpStatus.INTERNAL_SERVER_ERROR)
}
```

---

## reference
- https://docs.spring.io/spring-cloud-circuitbreaker/reference/spring-cloud-circuitbreaker-resilience4j.html
