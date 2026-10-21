# warmup

## 배포 직후 지연 원인

- 클래스 지연 로딩: 첫 요청 시점에 jackson, mvc, jpa 등 클래스 로딩으로 인한 지연
- JIT 미컴파일: 인터프리터 -> C1 -> C2 단계적 컴파일에서 인터프리터에 의해 메서드 바이트코드 실행되므로 지연
- 커넥션 풀: 기동 직후 커넥션 생성 대기

## 적용 기준

- 대상: 배포 직후 호출 비중 상위 api
- 조회 GET API만 예열, POST/DELETE/PATCH/PUT 등 api 제외

## 트래픽 차단 원리

```
ApplicationRunner (WarmupRunner)
-> ApplicationReadyEvent
-> ReadinessState.ACCEPTING_TRAFFIC
```

- Runner 종료 전까지 readiness 실패 유지
- 동기 실행이라 별도 HealthIndicator 불필요

## 구성

| task | 방식 | 대상 | 횟수 |
|---|---|---|---|
| `WebWarmupTask` | localhost HTTP 호출 | Tomcat, MVC, Jackson | 100 |
| `ProductWarmupTask` | `ProductService` 직접 호출 | 트랜잭션, JPA, JDBC | 2000 |

- 대상 API: 목록, 상세
- 신규 대상은 `WarmupTask` 구현 빈 추가로 자동 등록
- 변경 API 첫 호출: 요청 역직렬화, JPA 쓰기 경로 초기화 비용 잔존

## 측정

```shell
./gradlew bootJar
./measure.sh no-warmup
./measure.sh
```

| | 첫 요청 | 이후 요청 |
|---|---|---|
| no-warmup | 200ms | 4~5ms |
| HTTP 단일 (2000회, 약 10초) | 2.4ms | 2~3ms |
| 계층 분리 (약 0.9초) | 1.9~2.4ms | 2~3ms |

| | 웜업 | 첫 요청 | readiness 이후 클래스 로딩 | readiness 이후 C2 컴파일 | 부하 p99 (cold) | 부하 p99 (warm) |
|---|---|---|---|---|---|---|
| no-warmup | - | 186~202ms | 약 1100개 | 약 700개 | 25~75ms | 13~38ms |
| warmup | 0.9~1.0초 | 2.4~2.7ms | 5~8개 | 150~230개 | 8~14ms | 7~8ms |

- 측정 환경: 로컬 macOS, H2 메모리 DB, virtual thread 활성화
- 부하: readiness 직후 `ab -n 3000 -c 20` 2회 연속 (cold, warm)
- 측정 옵션: `-Xlog:class+load`, `-XX:+PrintCompilation`

## 지표

| 지표 | 내용 |
|---|---|
| `jvm.classes.loaded` | 로딩된 클래스 수 |
| `jvm.compilation.time` | JIT 컴파일 누적 시간 |

- 판단 기준: readiness 직후 대비 클래스 로딩, JIT 컴파일 증가량
- `measure.sh` 종료 시 증가량 출력
- 웜업 소요 시간: `warmup completed` 로그

## 주의사항

- 웜업 시간만큼 readiness 전환 지연
- 트래픽 패턴 변경 시 웜업 횟수 재측정 필요
- 낮은 CPU limit 에서 JIT 스레드 경합으로 웜업 시간 증가

## 참고

- [카카오페이 - 배포 직후 응답 지연 해결 (JVM 웜업)](https://tech.kakaopay.com/post/jvm-warm-up/)
- [올리브영 - Spring Boot 웜업 로직 최적화](https://oliveyoung.tech/2024-10-30/application_warmup_algorithm/)
- [Spring Boot - Application Availability](https://docs.spring.io/spring-boot/reference/features/spring-application.html)
- [Spring Boot - AOT Cache](https://docs.spring.io/spring-boot/4.0/reference/packaging/aot-cache.html)
