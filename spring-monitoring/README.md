# spring-monitoring

Spring Boot 4 + Kotlin 애플리케이션을 **Grafana LGTM+P 스택**으로 모니터링하는 최소 샘플.

`LGTM` = Loki/Grafana/Tempo/Mimir(Grafana 공식 관측 스택), `+P` = **Pyroscope(continuous profiling)**.
관측가능성의 네 기둥 — metrics · logs · traces · **profiles** — 을 모두 다룬다.

| 약자 | 컴포넌트 | 역할 | 수집 방식 |
|------|----------|------|-----------|
| **L** | Loki | 로그 | 앱이 loki4j 로 직접 push (`:3100`) |
| **G** | Grafana | 대시보드 / 상호연동 | `:3000` |
| **T** | Tempo | 트레이스 | 앱이 OTLP/HTTP push (`:4318`) |
| **M** | Mimir | 메트릭 장기 저장 | Prometheus `remote_write` (`:9009`) |
| **P** | **Pyroscope** | **프로파일** | **앱이 pyroscope agent로 push (`:4040`)** |

> 메트릭 스크랩에는 **Prometheus**(`:9090`)가 함께 쓰인다 — 앱 `/actuator/prometheus`를 pull 한 뒤 Mimir로 `remote_write` 하는 보조 역할이다.

## 아키텍처

```
                         ┌─────────────┐  scrape /actuator/prometheus
                         │ Prometheus  │◀──────────────────────────────┐
                         └──────┬──────┘                               │
                       remote_write                                    │
   ┌─────────┐          ┌──────▼──────┐                               │
   │ Grafana │◀─────────│    Mimir    │  metrics                       │
   │  :3000  │◀── Tempo     (traces)  ◀── OTLP ──┐         ┌────────────────────────┐
   │         │◀── Loki      (logs)    ◀── push ──┤         │  Spring app (local)     │
   │         │◀── Pyroscope (profiles)◀── push ──┴─────────│  ./gradlew bootRun :8080│
   └─────────┘                                             └────────────────────────┘
```

- 앱은 **로컬에서 실행**(`./gradlew bootRun`), 백엔드 7종은 **docker compose**.
- 메트릭은 pull(Prometheus 스크랩) → Mimir 저장, 트레이스/로그/프로파일은 앱이 직접 push.

## 기술 스택

- Spring Boot 4.1.0 / Kotlin 2.3.21 / Java 25 / Gradle 9.2.1
- MySQL 8.4 + Spring Data JPA
- Micrometer (Prometheus registry) + Micrometer Tracing (OTLP bridge) + loki4j + Pyroscope agent

## 클린 아키텍처

```
com.study.monitoring
├── config      PyroscopeConfig      (프로파일링 에이전트 기동)
└── order
    ├── ui          OrderController   (REST, 엔티티 대신 DTO 노출)
    ├── application OrderService      (@Transactional, 커스텀 메트릭/로그)
    └── domain      Order, repository/OrderRepository
```

## 실행 방법

### 1. 모니터링 백엔드 기동

```bash
docker compose up -d
```

기동 확인: Grafana http://localhost:3000 (익명 Admin 로그인)

### 2. 앱 실행

```bash
./gradlew bootRun
```

### 3. 트래픽 발생

```bash
# 주문 생성 (메트릭 증가 + 트레이스 + 로그)
curl -X POST localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"product":"book","amount":3}'

# 주문 조회
curl localhost:8080/api/orders
```

## 확인 포인트

| 신호 | 확인 방법 |
|------|-----------|
| 메트릭 | `curl localhost:8080/actuator/prometheus` → Prometheus(:9090) target `UP` → Grafana **Mimir** 에서 `orders_created_total` 쿼리 |
| 트레이스 | Grafana **Tempo** 에서 `controller → service → jdbc` 스팬 확인 |
| 로그 | Grafana **Loki** 에서 `{app="spring-monitoring"}` 조회, 동일 traceId 포함 |
| 프로파일 | Grafana **Pyroscope** 에서 `spring-monitoring` 서비스 flame graph 확인 (`:4040` UI에서도 가능) |
| 연동 | Tempo 트레이스 → Logs 버튼으로 Loki 이동, Loki 로그의 TraceID → Tempo 이동 |

## 정리

```bash
docker compose down -v
```

## 참고: Spring Boot 4 관련 주의점

- 트레이스 OTLP 키가 변경됨: (Boot3) `management.otlp.tracing.endpoint` → (Boot4) `management.opentelemetry.tracing.export.otlp.endpoint`
- 액추에이터 엔드포인트 노출: `management.endpoint.<id>.enabled` → `management.endpoint.<id>.access`
- 메트릭은 `spring-boot-starter-opentelemetry`(OTLP push) 대신 `micrometer-registry-prometheus`(scrape) 사용 — Prometheus 스크랩 경로 유지 목적
