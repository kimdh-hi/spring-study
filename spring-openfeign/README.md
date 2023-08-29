## Spring OpenFeign

재시도
- default: Retryer.NEVER_RETRY (재시도 안 함)
- 응답 예외 코드에 따른 `RetryableException()` 예외 지정 필요
  - `RetryableException` 발생 시에만 재시도 수행
  - `RetryableException` 는 응답 status code 가 음수이거나 connection 자체에 실패한 경우 응답
    - 즉, 위 두 경우를 제외하고 다른 예외응답은 `RetryableException` 를 응답하지 않으므로 재시도 대상이 아님
  - 재시도를 원한다면 특정 예외코드 응답시 `RetryableException` 로 응답해줘야 함
```kt
  @Bean
  fun decoder(): ErrorDecoder {
    return ErrorDecoder { _: String, response: Response ->
      when(response.status()) {
        // retryAfter 에 Date() 지정시 지정한 retry 시간만큼 늘려가며 수행 안 됨 (예제 코드에 이런 코드가 많음..)
        // Retryer.continueOrPropagate 참고 (Date() 지정시 interval 이 음수가 됨)
        HttpStatus.INTERNAL_SERVER_ERROR.value() -> RetryableException(response.status(), response.reason(), response.request().httpMethod(), null, response.request())
        else -> IllegalStateException("feignClient generic error")
      }
    }
  }
```


feignClient 관련 자동설정 bean
- `FeignClientsConfiguration`

feignClientConfiguration
- `FeignClient` 설정 관련 빈을 올리는 경우 `@Configuration` 을 사용하게 되면 모든 FeignClient 에 적용된다.
- 특정 `FeignClient` 에만 적용하고 싶은 설정이 있다면 `@FeignClient` 의 configuration 에 `@Configuration` 이 붙지 않은 클래스를 설정한다.
  - `@Configuration` 에 설정한 설정과 충돌되는 경우 `@FeignClient` 의 configuration 에 설정한 빈이 우선순위를 가진다.


---

### 참고
https://engineering.getmidas.com/using-spring-retryable-with-feign-client-methods-9f77e509ad55 <br/>
https://techblog.woowahan.com/2657/