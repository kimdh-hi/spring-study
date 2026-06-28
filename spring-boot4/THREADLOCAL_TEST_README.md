# Virtual Thread에서 ThreadLocal과 SecurityContext 전파 테스트

## 개요
Virtual Thread 환경에서 Filter에서 설정한 ThreadLocal 값과 SecurityContext 값이 RestClientLoggingInterceptor까지 유지되는지 확인하는 테스트입니다.

## 구현 내용

### 1. Virtual Thread 활성화
- `application.yml`에서 `spring.threads.virtual.enabled: true` 설정

### 2. RequestContextHolder (ThreadLocal 관리)
- `RequestContextHolder.kt`: ThreadLocal을 사용하여 요청 컨텍스트 관리
  - `requestId`: 요청 식별자
  - `userInfo`: 사용자 정보

### 3. ThreadLocalAndSecurityFilter
- `ThreadLocalAndSecurityFilter.kt`: 필터에서 ThreadLocal과 SecurityContext 설정
  - ThreadLocal에 requestId, userInfo 저장
  - SecurityContext에 인증 정보 저장
  - 로그로 현재 스레드 정보 출력

### 4. RestClientLoggingInterceptor (수정됨)
- `HttpClientConfig.kt`의 RestClientLoggingInterceptor 수정
  - HTTP 클라이언트 호출 시 ThreadLocal과 SecurityContext 값 확인
  - 로그로 값이 유지되는지 확인

### 5. ThreadLocalTestController
- `ThreadLocalTestController.kt`: 테스트용 엔드포인트
  - 컨트롤러에서 ThreadLocal과 SecurityContext 값 확인
  - HTTP 클라이언트 호출
  - 호출 후 다시 ThreadLocal과 SecurityContext 값 확인

## 테스트 방법

### 1. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 2. 테스트 엔드포인트 호출

#### 방법 1: .http 파일 사용 (권장)
IntelliJ IDEA나 VS Code의 REST Client 플러그인으로 `test-api.http` 파일을 열어서 실행

#### 방법 2: curl 명령어
```bash
# 기본 테스트
curl http://localhost:8080/test/threadlocal

# 다중 HTTP 호출 테스트
curl http://localhost:8080/test/threadlocal/multiple

# 비동기 처리 테스트
curl http://localhost:8080/test/threadlocal/async

# POST 요청 테스트
curl -X POST http://localhost:8080/test/threadlocal \
  -H "Content-Type: application/json" \
  -d '{"name":"테스트","message":"ThreadLocal 테스트"}'

# 지연 시간 테스트
curl http://localhost:8080/test/threadlocal/delay?delayMs=2000

# ThreadLocal 정보만 조회
curl http://localhost:8080/test/threadlocal/info
```

### 3. 로그 확인
애플리케이션 로그에서 다음을 확인:

```
[Filter] ThreadId=..., VirtualThread=true, RequestId=..., UserInfo=..., SecurityUser=testUser
[Controller] ThreadId=..., VirtualThread=true, RequestId=..., UserInfo=..., SecurityUser=testUser
[RestClientLoggingInterceptor] ThreadId=..., VirtualThread=true, RequestId=..., UserInfo=..., SecurityUser=testUser
[Controller After HTTP Call] ThreadId=..., VirtualThread=true, RequestId=..., UserInfo=..., SecurityUser=testUser
```

## 확인 사항

### ✅ 성공 케이스
- 모든 로그에서 `VirtualThread=true`로 표시됨
- Filter → Controller → RestClientLoggingInterceptor → Controller After에서 모두 같은 RequestId, UserInfo, SecurityUser 값이 유지됨
- ThreadId는 다를 수 있지만, ThreadLocal과 SecurityContext 값은 유지되어야 함

### ❌ 실패 케이스
- RestClientLoggingInterceptor에서 RequestId, UserInfo, SecurityUser가 null로 표시됨
- 이는 ThreadLocal이나 SecurityContext가 전파되지 않았음을 의미

## API 엔드포인트

| 엔드포인트 | 메서드 | 설명 |
|-----------|--------|------|
| `/test/threadlocal` | GET | 기본 ThreadLocal + SecurityContext 전파 테스트 (1회 HTTP 호출) |
| `/test/threadlocal/multiple` | GET | 여러 번 HTTP 클라이언트 호출 테스트 (3회 호출) |
| `/test/threadlocal/async` | GET | 코루틴을 사용한 비동기 처리 테스트 (ThreadLocal 전파 안 될 수 있음) |
| `/test/threadlocal` | POST | POST 요청으로 ThreadLocal 테스트 |
| `/test/threadlocal/delay?delayMs=1000` | GET | 지연 시간 추가 테스트 (Virtual Thread 효율성 확인) |
| `/test/threadlocal/error` | GET | 에러 발생 시 ThreadLocal 정리 확인 |
| `/test/threadlocal/info` | GET | ThreadLocal 정보만 조회 (HTTP 호출 없음) |

## 주요 클래스

| 클래스명 | 경로 | 역할 |
|---------|------|------|
| RequestContextHolder | context/RequestContextHolder.kt | ThreadLocal 관리 |
| ThreadLocalAndSecurityFilter | filter/ThreadLocalAndSecurityFilter.kt | ThreadLocal, SecurityContext 설정 |
| HttpClientConfig | config/HttpClientConfig.kt | RestClientLoggingInterceptor 포함 |
| ThreadLocalTestController | controller/ThreadLocalTestController.kt | 테스트 엔드포인트 |
| SecurityConfig | config/SecurityConfig.kt | Spring Security 설정 |

## Resilience4j와 ThreadLocal 전파 이슈 및 해결

### 문제 상황
`spring-cloud-starter-circuitbreaker-resilience4j` 의존성을 추가하면:
- Resilience4j가 기본적으로 **ThreadPoolBulkhead**를 사용하여 별도 스레드 풀에서 요청을 실행
- ThreadLocal 값 (RequestContextHolder, SecurityContext)이 새로운 스레드로 전파되지 않음
- RestClientLoggingInterceptor에서 ThreadLocal 값이 null로 표시됨

### 해결 방법

#### 방법 1: Micrometer Context Propagation 사용 (권장)
**설정 파일**: `ContextPropagationConfig.kt`

Micrometer Context Propagation의 `ThreadLocalAccessor`를 구현하여 자동으로 ThreadLocal 값을 전파:
- `RequestIdAccessor`: RequestId 전파
- `UserInfoAccessor`: UserInfo 전파
- `SecurityContextAccessor`: SecurityContext 전파

```kotlin
@PostConstruct
fun setupContextPropagation() {
  ContextRegistry.getInstance().registerThreadLocalAccessor(RequestIdAccessor())
  ContextRegistry.getInstance().registerThreadLocalAccessor(UserInfoAccessor())
  ContextRegistry.getInstance().registerThreadLocalAccessor(SecurityContextAccessor())
}
```

**장점**:
- Spring Boot 4의 표준 방식
- Spring Security, Reactor, Resilience4j 등 다양한 라이브러리와 호환
- Virtual Thread 환경에서도 자동으로 작동

#### 방법 2: Resilience4j ContextPropagator 사용
**설정 파일**: `Resilience4jContextPropagatorConfig.kt`

Resilience4j 전용 `ContextPropagator` 구현:
```kotlin
@Component
class RequestContextPropagator : ContextPropagator<RequestContextPropagator.ContextSnapshot> {
  // ThreadLocal 값을 캡처하고 전파
}
```

**application.yml 설정**:
```yaml
resilience4j:
  thread-pool-bulkhead:
    configs:
      default:
        contextPropagators:
          - com.study.springboot4.config.RequestContextPropagator
```

#### 방법 3: SemaphoreBulkhead 사용 (가장 간단)
**application.yml 설정**:
```yaml
resilience4j:
  bulkhead:
    configs:
      default:
        maxConcurrentCalls: 10
        maxWaitDuration: 0
```

**장점**:
- Thread Pool 대신 Semaphore를 사용하여 같은 스레드에서 실행
- ThreadLocal 전파 문제가 발생하지 않음
- 설정이 간단함

**단점**:
- Thread Pool의 격리 기능 (Thread isolation)을 사용할 수 없음

### 관련 이슈
- [Resilience4j #742 - Propagate distributed tracing information](https://github.com/resilience4j/resilience4j/issues/742)
- [Spring Security #13123 - Micrometer Context Propagation for SecurityContext](https://github.com/spring-projects/spring-security/issues/13123)
- [Spring Security #16665 - Automatic context-propagation with Micrometer](https://github.com/spring-projects/spring-security/issues/16665)
- [Spring Cloud OpenFeign #949 - Loss of ThreadLocal with Resilience4j](https://github.com/spring-cloud/spring-cloud-openfeign/issues/949)

## 참고사항

### Virtual Thread와 ThreadLocal
- Spring Boot 3.2+에서는 Virtual Thread를 지원합니다
- Virtual Thread는 경량 스레드로, 많은 수의 동시 요청을 효율적으로 처리할 수 있습니다
- Spring은 Virtual Thread 환경에서도 ThreadLocal과 SecurityContext를 자동으로 전파합니다
- 단, 비동기 작업이나 별도 스레드풀 사용 시에는 추가 설정이 필요할 수 있습니다

### 주의사항
- ThreadLocal은 요청 처리 후 반드시 `clear()`로 정리해야 메모리 누수를 방지할 수 있습니다
- SecurityContext도 요청 처리 후 `clearContext()`로 정리해야 합니다
- 현재 구현에서는 Filter의 `finally` 블록에서 정리하고 있습니다
- Resilience4j를 사용할 때는 반드시 Context Propagation 설정을 확인하세요
